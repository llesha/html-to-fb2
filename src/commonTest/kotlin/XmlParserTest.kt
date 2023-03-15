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

    @Test
    fun parseFragment() {
        val text = """<p><strong>Keep salaries secret</strong>. 
            It’s obvious: Don’t let them discuss 
            <a href="/2015/07/21/hourly-pay-modern-slavery.html">salaries</a>
            . They must keep this information secret. Warn them or even sign 
            <a href="/2015/05/04/how-to-protect-business-idea.html">NDAs</a> 
            prohibiting any talks about wages, bonuses, compensation plans, etc. 
            They must feel that this information is toxic and 
            never even talk to each other about salaries. 
            If they don’t know how much their coworkers are getting, 
            they won’t raise salary questions for much longer.</p>"""

        val (_, el) = XmlParser().parse(text)
        val tag = TagFactory().createTag(el)
        tag.getFb2(HtmlTranslator("", WriteStream("C:\\Users\\alex\\Desktop\\book.fb2")))
    }
}
