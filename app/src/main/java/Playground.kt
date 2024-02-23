import java.sql.DriverManager.println
import kotlin.math.pow

fun accumulatedAmount(principal: Double, rate: Double, periodsPerYear: Int, years: Int): Double {
    return principal * (1 + rate / periodsPerYear).pow(periodsPerYear * years)
}

fun findCrossoverTime(principal: Double, rate1Initial: Double, rate1Final: Double, rate2: Double, periodsPerYear: Int): Int {
    var time = 0
    while (true) {
        val amount1 = accumulatedAmount(principal, rate1Initial, periodsPerYear, time)
        val amount2 = accumulatedAmount(principal, rate2, periodsPerYear, time)
        if (amount2 > amount1) {
            return time
        }
        time++
    }
}

fun main() {
    // Constants
    val principalAmount = 100.0  // Let's assume $100 for simplicity
    val rate1Initial = 0.055  // 5.5%
    val rate1Final = 0.05  // 5.0%
    val rate2 = 0.0515  // 5.15%
    val periodsPerYear = 12  // Monthly compounding

    // Find crossover time
    val crossoverTime = findCrossoverTime(principalAmount, rate1Initial, rate1Final, rate2, periodsPerYear)
    println("The second account becomes more profitable than the first one after approximately $crossoverTime months.")
}
