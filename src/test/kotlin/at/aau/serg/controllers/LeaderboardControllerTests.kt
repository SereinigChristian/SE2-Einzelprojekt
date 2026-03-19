package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.web.server.ResponseStatusException
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null)

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeSorting() {
        val first = GameResult(1, "first", 20, 5.0)
        val second = GameResult(2, "second", 20, 10.0)
        val third = GameResult(3, "third", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null)

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }
        @Test
        fun test_getLeaderboard_invalidRank_throwsException() {
            whenever(mockedService.getGameResults()).thenReturn(listOf(GameResult(1, "p1", 10, 10.0)))

            // Exception bei Rank 0
            assertThrows<ResponseStatusException> {
                controller.getLeaderboard(0)
            }

            // Exception für zu hohen rank
            assertThrows<ResponseStatusException> {
                controller.getLeaderboard(99)
            }
        }
            @Test
            fun test_getLeaderboard_withValidRank() {
                // 10 Ergebnisse
                val results = (1..10).map { GameResult(it.toLong(), "Player$it", 100 - it, it.toDouble()) }
                whenever(mockedService.getGameResults()).thenReturn(results)

                // Abfrage für Rank 5
                val res = controller.getLeaderboard(5)

                assertEquals(7, res.size)
                assertEquals("Player2", res.first().playerName)
                assertEquals("Player8", res.last().playerName)
    }

}