package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    @GetMapping
    fun getLeaderboard(@RequestParam(required = false) rank: Int?): List<GameResult> {
        //Sortierung
        val sortedList = gameResultService.getGameResults()
            .sortedWith(compareBy({ -it.score }, { it.timeInSeconds }))

        //Aufgabe 2.2.2
        if (rank == null) return sortedList

        if (rank < 1 || rank > sortedList.size) {
            throw org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST, "Ungültiger Rank"
            )
        }
        val index = rank - 1
        val start = (index - 3).coerceAtLeast(0)
        val end = (index + 3).coerceAtMost(sortedList.size - 1)

        return sortedList.subList(start, end + 1)

    }
}