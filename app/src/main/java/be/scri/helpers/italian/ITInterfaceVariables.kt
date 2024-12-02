package be.scri.helpers.italian

object ITInterfaceVariables {
    // Currency Symbol and Alternates
    const val CURRENCY_SYMBOL = "€"
    val CURRENCY_SYMBOL_ALTERNATES = listOf("€", "$", "£", "¥", "₩", "¢")

    // Keyboard Labels
    const val SPACE_BAR = "spazio"
    const val LANGUAGE = "Italiano"
    const val INVALID_COMMAND_MSG = "Non in Wikidata"
    val BASE_AUTOSUGGESTIONS = listOf("ho", "non", "ma")
    val NUMERIC_AUTOSUGGESTIONS = listOf("utenti", "anni", "e")

    // Translate Command Texts
    const val TRANSLATE_KEY_LBL = "Tradurre"
    const val TRANSLATE_PLACEHOLDER = "Inserisci una parola"
    const val TRANSLATE_PROMPT = "it -› targetLanguage()" // Example, replace with actual language code
    const val TRANSLATE_PROMPT_AND_CURSOR = "$TRANSLATE_PROMPT commandCursor" // Replace with actual dynamic value when available
    const val TRANSLATE_PROMPT_AND_PLACEHOLDER = "$TRANSLATE_PROMPT_AND_CURSOR $TRANSLATE_PLACEHOLDER"

    // Conjugate Command Texts
    const val CONJUGATE_KEY_LBL = "Coniugare"
    const val CONJUGATE_PLACEHOLDER = "Inserisci un verbo"
    const val CONJUGATE_PROMPT = "Coniugare: "
    const val CONJUGATE_PROMPT_AND_CURSOR = "$CONJUGATE_PROMPT commandCursor" // Replace with actual value
    const val CONJUGATE_PROMPT_AND_PLACEHOLDER = "$CONJUGATE_PROMPT_AND_CURSOR $CONJUGATE_PLACEHOLDER"

    // Plural Command Texts
    const val PLURAL_KEY_LBL = "Plurale"
    const val PLURAL_PLACEHOLDER = "Inserisci un nome"
    const val PLURAL_PROMPT = "Plurale: "
    const val PLURAL_PROMPT_AND_CURSOR = "$PLURAL_PROMPT commandCursor" // Replace with actual value
    const val PLURAL_PROMPT_AND_PLACEHOLDER = "$PLURAL_PROMPT_AND_CURSOR $PLURAL_PLACEHOLDER"

    // Already Plural Message
    const val ALREADY_PLURAL_MSG = "Già plurale"
}
