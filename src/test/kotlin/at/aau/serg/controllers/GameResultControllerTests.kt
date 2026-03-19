package at.aau.serg.controllers
import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.Mockito.`when` as whenever
import kotlin.test.assertEquals
import kotlin.test.assertNull
class GameResultControllerTests {
    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController
    @BeforeEach
    fun setup() {
        mockedService = mock(GameResultService::class.java)
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_getAllGameResults() {
        val list = listOf(GameResult(1, "P1", 100, 10.0))
        whenever(mockedService.getGameResults()).thenReturn(list)

        val res = controller.getAllGameResults()

        assertEquals(1, res.size)
        verify(mockedService).getGameResults()
    }

    @Test
    fun test_getGameResult_exists() {
        val result = GameResult(1, "P1", 100, 10.0)
        whenever(mockedService.getGameResult(1L)).thenReturn(result)

        val res = controller.getGameResult(1L)

        assertEquals("P1", res?.playerName)
    }

    @Test
    fun test_getGameResult_notExists() {
        whenever(mockedService.getGameResult(99L)).thenReturn(null)

        val res = controller.getGameResult(99L)

        assertNull(res)
    }

    @Test
    fun test_addGameResult() {
        val result = GameResult(1, "P1", 100, 10.0)

        controller.addGameResult(result)

        verify(mockedService).addGameResult(result)
    }

    @Test
    fun test_deleteGameResult() {
        controller.deleteGameResult(1L)

        verify(mockedService).deleteGameResult(1L)
    }
}