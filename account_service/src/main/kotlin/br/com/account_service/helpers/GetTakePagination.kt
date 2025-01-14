package br.com.account_service.helpers

object GetTakePagination {
    fun getTake(skip: Int, take: Int): Int {
        if (skip < 0 || take < 0)
            throw ErrorResponse("Invalid page", 400)

        if (skip == 0)
            return take

        return skip * take
    }

    fun getSkip(skip: Int): Int {
        if (skip < 0 )
            throw ErrorResponse("Invalid page", 400)

        return skip
    }

    fun getPage(total: Long, skip: Int, take: Int): Int {
        if (skip < 0 || take < 0)
            throw ErrorResponse("Invalid page", 400)

        if (total == 0L)
            return 0

        return (skip / take).toInt()
    }
}