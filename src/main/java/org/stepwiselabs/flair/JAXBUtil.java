package org.stepwiselabs.flair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stepwiselabs.flair.exceptions.BadDataException;
import org.stepwiselabs.flair.exceptions.ResourceAccessException;
import org.stepwiselabs.flair.resource.ReadableResource;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.XMLFilterImpl;

import javax.xml.bind.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility for parsing XML into JAXB POJOS
 */
public class JAXBUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(JAXBUtil.class);

    public JAXBUtil() {
        throw new InstantiationError();
    }

    /**
     * Parse the XML from the given {@link ReadableResource} into the parameterized JAXB POJO.
     *
     * @param rr - The {@link ReadableResource} to read from
     * @param defaultNamespace - The default namespace to apply to the XML
     * @param objectFactoryClass - JAXB object factory class
     * @param <T>
     * @return
     */
    public static <T> T parse(ReadableResource rr, String defaultNamespace, Class<T> objectFactoryClass) {
        if (rr == null) {
            throw new IllegalArgumentException("The JAXB resource is null");
        }
        try (InputStream in = rr.open()) {
            JAXBContext jc = JAXBContext.newInstance(objectFactoryClass);

            // Set the parent XML Reader on the Filter
            XMLFilter filter = new DefaultNamespaceFilter(defaultNamespace);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            filter.setParent(sp.getXMLReader());

            // Set UnmarshallerHandler as ContextHandler on XMLFilter
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            UnmarshallerHandler unmarshallerHandler = unmarshaller.getUnmarshallerHandler();
            filter.setContentHandler(unmarshallerHandler);

            unmarshaller.setEventHandler(event -> {
                ValidationEventLocator locator = event.getLocator();
                String url = locator.getURL() != null ? locator.getURL().toString() : null;
                String message = String.format("Caught Validation Event {file=%s, line=%s, column=%s, message=%s",
                        url, locator.getLineNumber(), event.getMessage());

                if (event.getLinkedException() != null) {
                    Throwable linkedException = event.getLinkedException();
                    message += String.format(", exception=%s, exceptionMessage=%s}",
                            linkedException.getClass().getSimpleName(), linkedException.getMessage());
                } else {
                    message += "}";
                }
                LOGGER.error(message);
                return false;
            });

            filter.parse(new InputSource(in));
            JAXBElement<T> element = (JAXBElement<T>) unmarshallerHandler.getResult();
            return element.getValue();

        } catch (IOException e) {

            // transient exception
            String msg = String.format("Caught '%s' while parsing resource '%s' with namespace '%s' : %s",
                    e.getClass().getSimpleName(), rr, defaultNamespace, e.getMessage());
            LOGGER.error(msg, e);
            throw new ResourceAccessException(e, msg);

        } catch (Exception e) {

            // non-transient exception
            String msg = String.format("Cannot parse resource '%s' with namespace '%s' : %s",
                    rr, defaultNamespace, e.getMessage());
            LOGGER.error(msg);
            throw new BadDataException(e, msg);
        }

    }

    /**
     * An {@link XMLFilter} to be used with JAXB's SAX parse.  It will apply the
     * default namespace when one does not exist.
     */
    private static class DefaultNamespaceFilter extends XMLFilterImpl {

        private final String defaultNamespace;

        public DefaultNamespaceFilter(String defaultNamespace) {
            this.defaultNamespace = defaultNamespace;
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {

            String namespace = Strings.isBlank(uri) ? defaultNamespace : uri;
            super.endElement(namespace, localName, qName);
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes atts) throws SAXException {

            String namespace = Strings.isBlank(uri) ? defaultNamespace : uri;
            super.startElement(namespace, localName, qName, atts);
        }
    }

}
