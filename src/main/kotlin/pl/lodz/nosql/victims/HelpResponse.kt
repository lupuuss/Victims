package pl.lodz.nosql.victims

object HelpResponse {

    val description = "API provides data of people killed by the police in the United States."
    val database = "MongoDB"

    val requests = mapOf(
            "GET /" to "Returns this response.",
            "GET /victim" to "Returns victims. By default response is limited to 100 records and skips none." +
                    " Params: limit - limits response to n records; offset - skips first n records;" +
                    " sortBy - name of property to sort records; desc - if true, sorts descending;",
            "POST /victim" to "Adds passed victim to the database. 'date' and 'id' are ignored.",
            "PATCH /victim/id/:id" to "Updates victim with :id. Only mentioned fields are updated.",
            "DELETE /victim/id/:id" to "Removes victim with :id.",
            "GET /victim/id/:id" to "Returns victim with :id",
            "GET /victim/between/date" to "Returns all victims between two dates." +
                    " Params: from - date; to - date",
            "GET /victim/:property/:value" to "Returns victims filtered by passed property and its value." +
                    " Params: limit - limits response to n records; offset - skips first n records;" +
                    " sortBy - name of property to sort records; desc - if true, sorts descending;"
    )
}