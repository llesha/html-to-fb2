import kotlin.test.Test

class Fb2DocumentTest {
    @Test
    fun testTranslate() {
        val fb2Document = Fb2Document(
            "https://www.yegor256.com/contents.html",
            "C:\\Users\\alex\\Desktop\\book.fb2"
        )
        Constants.isTree = false
        fb2Document.create()
    }
}
