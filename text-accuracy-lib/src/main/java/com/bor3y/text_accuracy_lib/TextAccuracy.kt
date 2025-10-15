package com.bor3y.text_accuracy_lib

class TextAccuracy {

    enum class OperationType {
        MATCH,
        ALMOST,
        SUBSTITUTE,
        DELETE,
        INSERT,
        SWAPPED,
        PLURAL
    }

    data class Operation(
        val type: OperationType,
        val actualWord: String? = null,
        val userWord: String? = null,
        val actualWords: Pair<String, String>? = null,
        val userWords: Pair<String, String>? = null
    )

    private fun normalize(word: String): String = word.lowercase()

    private fun stripPunctuation(word: String): String =
        word.replace(Regex("[.,!?;:\"'()\\-]"), "")

    private fun tokenize(text: String): List<String> =
        text.trim().split(Regex("\\s+"))

    private fun levenshteinDistance(a: String, b: String): Int {
        val m = a.length
        val n = b.length
        val dp = Array(m + 1) { IntArray(n + 1) }

        for (i in 0..m) dp[i][0] = i
        for (j in 0..n) dp[0][j] = j

        for (i in 1..m) {
            for (j in 1..n) {
                val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,     // deletion
                    dp[i][j - 1] + 1,     // insertion
                    dp[i - 1][j - 1] + cost // substitution
                )

                if (i > 1 && j > 1 &&
                    a[i - 1] == b[j - 2] &&
                    a[i - 2] == b[j - 1]
                ) {
                    dp[i][j] = minOf(dp[i][j], dp[i - 2][j - 2] + cost)
                }
            }
        }
        return dp[m][n]
    }

    private fun diffWordsSmart(refText: String, userText: String): Pair<List<Operation>, Double> {
        val refWords = tokenize(refText)
        val userWords = tokenize(userText)
        val m = refWords.size
        val n = userWords.size

        val dp = Array(m + 1) { DoubleArray(n + 1) { Double.POSITIVE_INFINITY } }
        val backtrack = Array(m + 1) { arrayOfNulls<Operation>(n + 1) }

        dp[0][0] = 0.0
        for (i in 1..m) {
            dp[i][0] = i.toDouble()
            backtrack[i][0] = Operation(OperationType.DELETE, actualWord = refWords[i - 1])
        }
        for (j in 1..n) {
            dp[0][j] = j.toDouble()
            backtrack[0][j] = Operation(OperationType.INSERT, userWord = userWords[j - 1])
        }

        for (i in 1..m) {
            for (j in 1..n) {
                val refW = refWords[i - 1]
                val userW = userWords[j - 1]
                val refWNorm = normalize(refW)
                val userWNorm = normalize(userW)
                val refWStripped = stripPunctuation(refWNorm)
                val userWStripped = stripPunctuation(userWNorm)

                var cost: Double
                val op: Operation

                if (refWNorm == userWNorm) {
                    cost = 0.0
                    op = Operation(OperationType.MATCH, refW, userW)
                } else if (
                    refWStripped.endsWith("s") &&
                    !userWStripped.endsWith("s") &&
                    refWStripped == userWStripped + "s"
                ) {
                    cost = 0.3
                    op = Operation(OperationType.PLURAL, refW, userW)
                } else {
                    val d = levenshteinDistance(refWNorm, userWNorm)
                    if (refW.length > 2 && userW.length > 2 && d == 1) {
                        cost = 0.5
                        op = Operation(OperationType.ALMOST, refW, userW)
                    } else {
                        cost = 1.0
                        op = Operation(OperationType.SUBSTITUTE, refW, userW)
                    }
                }

                val subCost = dp[i - 1][j - 1] + cost
                val delCost = dp[i - 1][j] + 1
                val insCost = dp[i][j - 1] + 1
                var swapCost = Double.POSITIVE_INFINITY
                var opSwap: Operation? = null

                if (i > 1 && j > 1) {
                    val refFirst = stripPunctuation(normalize(refWords[i - 2]))
                    val refSecond = stripPunctuation(normalize(refWords[i - 1]))
                    val userFirst = stripPunctuation(normalize(userWords[j - 2]))
                    val userSecond = stripPunctuation(normalize(userWords[j - 1]))
                    if (levenshteinDistance(refFirst, userSecond) <= 1 &&
                        levenshteinDistance(refSecond, userFirst) <= 1
                    ) {
                        swapCost = dp[i - 2][j - 2] + 0.8
                        opSwap = Operation(
                            OperationType.SWAPPED,
                            actualWords = refWords[i - 2] to refWords[i - 1],
                            userWords = userWords[j - 2] to userWords[j - 1]
                        )
                    }
                }

                val minCost = minOf(subCost, delCost, insCost, swapCost)
                dp[i][j] = minCost
                backtrack[i][j] = when (minCost) {
                    subCost -> op
                    delCost -> Operation(OperationType.DELETE, actualWord = refWords[i - 1])
                    insCost -> Operation(OperationType.INSERT, userWord = userWords[j - 1])
                    swapCost -> opSwap
                    else -> null
                }
            }
        }

        var i = m
        var j = n
        val operations = mutableListOf<Operation>()
        while (i > 0 || j > 0) {
            val op = backtrack[i][j] ?: break
            when (op.type) {
                OperationType.MATCH,
                OperationType.ALMOST,
                OperationType.SUBSTITUTE,
                OperationType.PLURAL -> {
                    operations.add(op)
                    i--
                    j--
                }
                OperationType.DELETE -> {
                    operations.add(op)
                    i--
                }
                OperationType.INSERT -> {
                    operations.add(op)
                    j--
                }
                OperationType.SWAPPED -> {
                    operations.add(op)
                    i -= 2
                    j -= 2
                }
            }
        }
        operations.reverse()
        return operations to dp[m][n]
    }

    private fun calculateAccuracySmart(totalCost: Double, refText: String): Double {
        val refWords = tokenize(refText)
        val maxPossible = refWords.size.toDouble()
        return ((1 - totalCost / maxPossible) * 100).coerceAtLeast(0.0)
    }

    fun findAccuracy(actualText: String, userText: String): Map<Double, List<Operation>> {
        val (ops, totalCost) = diffWordsSmart(actualText, userText)
        val accuracy = calculateAccuracySmart(totalCost, actualText)

        return mapOf(accuracy to ops)
    }
}