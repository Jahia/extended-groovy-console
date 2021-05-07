import org.xml.sax.SAXParseException

private String generateLipsum() { generateLipsum("paras", 5, true) }

private String generateLipsum(int count) { generateLipsum("paras", count, true) }

private String generateLipsum(int count, boolean start) { generateLipsum("paras", count, start) }

/**
 * Use the lipsum generator to generate Lorem Ipsum dummy paragraphs / words / bytes.
 *
 * Lorem Ipsum courtesy of www.lipsum.com by James Wilson
 *
 * @param what in ['paras','words','bytes'], default: 'paras'
 * @param amount of paras/words/bytes, default: 2 (for words minimum is 5, for bytes it is 27)
 * @param start always start with 'Lorem Ipsum', default = true
 * */
private String generateLipsum(String what, int amount, boolean start) {


    def address = "https://www.lipsum.com/feed/xml?what=$what&amount=$amount&start=${start ? 'yes' : 'no'}"
    String xml = new URL(address).text

    try {
        def feed = new XmlSlurper().parseText(xml)

        return feed.lipsum.text()
    } catch (SAXParseException e) {
        logger.error("Error when generating Lorem Ipsum from " + address + " ,response: " + xml, e)
        return "Lorem Ipsum - Failed to generate more"
    }

}