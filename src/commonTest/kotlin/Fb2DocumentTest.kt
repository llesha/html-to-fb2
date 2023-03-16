import kotlin.test.Test

class Fb2DocumentTest {
    @Test
    fun testTranslate() {
        val fb2Document = Fb2Document("https://www.yegor256.com/2016/12/06/how-to-pay-programmers-less.html", "fb")
        fb2Document.create()
    }
}
