/**
 * Main entry point for this problem.
 */
fun main(args: Array<String>){

    if(args.size != 3){
        println("Invalid arguments passed!")
        println("Usage: java -jar <program> <first word> <second word> <shuffled>")
        return
    }

    val firstWord = args[0]
    val secondWord = args[1]
    val shuffledWord = args[2]

    val isShuffleValid = ShuffleValidator.isShuffleValid(firstWord, secondWord, shuffledWord)

    if(isShuffleValid){
        println("shuffle CORRECT")
    } else {
        println("shuffle INCORRECT")
    }

}