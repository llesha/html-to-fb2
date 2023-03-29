import core.HtmlTranslator
import core.WriteStream
import core.XmlParser
import tags.TagFactory
import kotlin.test.Test

class XmlParserTest {
    @Test
    fun parseTest() {
        val text = """<bookstore><book>
                    <title>Everyday Italian</title>
                    <author>Giada De Laurentiis</author>
                    <year>2005</year>
                    </book></bookstore>
                    """
        XmlParser().parse(text)
    }
}
