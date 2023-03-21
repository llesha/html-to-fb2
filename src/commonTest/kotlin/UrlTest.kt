import utils.getPageUrl
import utils.getSite
import utils.removeProtocol
import kotlin.test.Test
import kotlin.test.assertEquals

class UrlTest {
    @Test
    fun removeProtocolTest() {
        assertEquals("https://llesha.github.io/regina/syntax/#top-level-declarations".removeProtocol(),
        "llesha.github.io/regina/syntax/#top-level-declarations")
    }

    @Test
    fun getSiteTest() {
        assertEquals("https://llesha.github.io/regina/syntax/#top-level-declarations".getSite(),
        "llesha.github.io")
    }

    @Test
    fun getPageUrlTest() {
        assertEquals("https://llesha.github.io/regina/syntax/#top-level-declarations".getPageUrl(),
        "regina/syntax")
    }
}
