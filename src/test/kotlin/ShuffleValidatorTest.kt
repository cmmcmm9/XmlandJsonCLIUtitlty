import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ShuffleValidatorTest{

    @Test
    fun isShuffleValid(){
        //Test a valid shuffle
        var firstWord = "TOURNAMENT"
        var secondWord = "DINNER"
        var correctShuffledWord = "TDINOURNANMENTER"

        assertTrue(ShuffleValidator.isShuffleValid(firstWord, secondWord, correctShuffledWord))

        //Test another valid shuffle
        correctShuffledWord = "TOURNAMENTDINNER"
        assertTrue(ShuffleValidator.isShuffleValid(firstWord, secondWord, correctShuffledWord))

        /*
        Test another valid shuffle, but this time where both words have the next letter as the remainder.
        Both DINNER and TOURNAMENT have N left at index 7, but it should choose to pop off the remainder of DINNER.
         */
        correctShuffledWord = "DINTOURNERNAMENT"
        assertTrue(ShuffleValidator.isShuffleValid(firstWord, secondWord, correctShuffledWord))

        /*
        Same test as above, but ensure that the order words are passed in does not change result
         */
        correctShuffledWord = "DINTOURNERNAMENT"
        assertTrue(ShuffleValidator.isShuffleValid(secondWord, firstWord, correctShuffledWord))

        /*
        Test Correct Shuffle, change words.
         */
        firstWord = "foo"
        secondWord = "bar"
        correctShuffledWord = "fbaoro"

        assertTrue(ShuffleValidator.isShuffleValid(firstWord, secondWord,correctShuffledWord))

        //Test Invalid Shuffle, letter out of order
        var incorrectShuffledWord = "TIDNOURNANMENTER"
        assertFalse(ShuffleValidator.isShuffleValid(firstWord, secondWord, incorrectShuffledWord))

        //Test Invalid Shuffle, letter that is not in either words
        incorrectShuffledWord = "TDXNOURNANMENTER"
        assertFalse(ShuffleValidator.isShuffleValid(firstWord, secondWord, incorrectShuffledWord))
    }
}