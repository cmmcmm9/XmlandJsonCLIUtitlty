import java.util.*

/**
 * Kotlin Object to determine whether or not the given two original words and the shuffled version are valid.
 * If a text file named 'words.txt' exists in src/main/resources/ then it will first ensure that the given
 * two words are in the file and thus allowed (ignoring case) via [ShuffleValidator.isWordAllowed]. If the file does not exists, it will assume
 * that all words are valid. Validation of a shuffle is handled via [ShuffleValidator.isShuffleValid].
 */
object ShuffleValidator {

    private var allowedWords: List<String>? = null

    /**
     * Get 'words.txt' if it exists. If it does, read its lines into allowedWords.
     */
    init {
        val allowedWordsResource = kotlin.runCatching {
            javaClass.getResourceAsStream("/words.txt")
        }

        //ensure words are all lowercase
        allowedWordsResource.getOrNull()?.let { inputStream ->
            allowedWords = inputStream.bufferedReader().readLines().map { it.toLowerCase() }
        }
    }

    /**
     * Extension function to clone a queue of type T
     */
    private fun <T>Queue<T>.clone(): Queue<T>{
        return LinkedList(this)
    }

    /**
     * Function to count the number of next correct letters that [remainingCharQueue] has in [remainingShuffledWord].
     */
    private fun countNextCorrectOccurrences(remainingShuffledWord: String, remainingCharQueue: Queue<Char>): Int {
        var count = 0
        remainingShuffledWord.forEach {
            if(it == remainingCharQueue.poll()){
                count += 1
            } else return@forEach
        }
        return count
    }

    /**
     * Function to determine if a word is allowed. If [ShuffleValidator.allowedWords] is
     * not null, then it will use the [List.contains] method to validate if the word is allowed (ignores case).
     * Otherwise, return true if [ShuffleValidator.allowedWords] is null
     */
    private fun isWordAllowed(word: String): Boolean{
        return allowedWords?.contains(word.toLowerCase()) ?: true
    }

    /**
     * Function to determine if a given shuffle is valid, based on the two original words given.
     * Will return true if the shuffle is correct, otherwise false.
     * Will also inform the user if one of the given words is not allowed, or where the shuffle is incorrect.
     */
    fun isShuffleValid(firstWord: String, secondWord: String, shuffledWord: String): Boolean{

        //Not allowed word, return false
        if(!isWordAllowed(firstWord) || !isWordAllowed(secondWord)){
            println("Invalid words given. Please refer to words.txt for a valid list of words.")
            return false
        }

        //If length of shuffle differs from firstWord + secondWord length, then no need to do validation
        if(shuffledWord.length != (firstWord.length + secondWord.length)) return false

        //load words into a Char<Queue>
        val firstWordCharQueue = LinkedList(firstWord.map { it.toLowerCase() }) as Queue<Char>
        val secondWordCharQueue = LinkedList(secondWord.map { it.toLowerCase() }) as Queue<Char>

        /*
        Iterate through the shuffled word.
        If a Char is not present in the head of either queue, it is not a valid shuffle.
        If both char queues have the same char at the head, choose the one with the most correct next occurrences.
         */
        shuffledWord.map { it.toLowerCase() }.forEachIndexed { index, char ->
            when {
                char == firstWordCharQueue.peek() && char == secondWordCharQueue.peek() -> {
                    //get the remaining shuffled word, starting at the next index
                    val remainingShuffledWord = shuffledWord.substring(index + 1).toLowerCase()

                    //clone the remaining firstWordQueues, with the current head removed
                    val remainingFirstWordQueue = firstWordCharQueue.clone().also { it.poll() }
                    val remainingSecondWordQueue = secondWordCharQueue.clone().also { it.poll() }

                    //Count number of next correct occurrences
                    val nextCorrectInFirstWord = countNextCorrectOccurrences(remainingShuffledWord, remainingFirstWordQueue)
                    val nextCorrectInSecondWord = countNextCorrectOccurrences(remainingShuffledWord, remainingSecondWordQueue)

                    /*
                    If first word has greater next occurrences, choose that the firstWordCharQueue to poll.
                    Otherwise poll the second. In the case that they are equal, it does not matter which queue
                    we poll.
                     */
                    if(nextCorrectInFirstWord > nextCorrectInSecondWord){
                        firstWordCharQueue.poll()
                    } else{
                        secondWordCharQueue.poll()
                    }

                }

                char == firstWordCharQueue.peek() -> {
                    firstWordCharQueue.poll()
                }

                char == secondWordCharQueue.peek() -> {
                    secondWordCharQueue.poll()
                }

                else -> {
                    println("Found a letter that is out of order, or is not in either words!")
                    println("At index (starting at 0): $index")
                    println("Character: $char")
                    return false
                }
            }
        }

        //Got this far, shuffle has to be correct
        return true
    }
}