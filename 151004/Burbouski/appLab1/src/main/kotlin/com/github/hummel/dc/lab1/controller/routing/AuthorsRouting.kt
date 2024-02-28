package com.github.hummel.dc.lab1.controller.routing

import com.github.hummel.dc.lab1.bean.Response
import com.github.hummel.dc.lab1.dto.request.AuthorRequestTo
import com.github.hummel.dc.lab1.dto.request.AuthorRequestToId
import com.github.hummel.dc.lab1.service.AuthorService
import com.github.hummel.dc.lab1.util.respond
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authorsRouting() {
	val authorsService: AuthorService by inject()

	checkAuthors(authorsService)

	createAuthor(authorsService)
	deleteAuthor(authorsService)
	updateAuthor(authorsService)
	getAuthor(authorsService)
}

private fun Route.checkAuthors(authorsService: AuthorService) {
	get("/api/v1.0/authors") {
		val authors = authorsService.getAll()

		respond(isCorrect = {
			authors.isNotEmpty()
		}, onCorrect = {
			call.respond(status = HttpStatusCode.OK, authors)
		}, onIncorrect = {
			call.respond(status = HttpStatusCode.OK, Response(HttpStatusCode.OK.value))
		})
	}
}

private fun Route.createAuthor(authorsService: AuthorService) {
	post("/api/v1.0/authors") {
		val authorRequestTo = try {
			call.receive<AuthorRequestTo>()
		} catch (e: Exception) {
			null
		}

		val author = authorsService.create(authorRequestTo)

		respond(isCorrect = {
			author != null
		}, onCorrect = {
			call.respond(status = HttpStatusCode.Created, author ?: return@respond)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest,
				Response(HttpStatusCode.BadRequest.value)
			)
		})
	}
}

private fun Route.getAuthor(authorsService: AuthorService) {
	get("/api/v1.0/authors/{id?}") {
		val id = call.parameters["id"] ?: return@get call.respond(
			status = HttpStatusCode.BadRequest,
			message = Response(HttpStatusCode.BadRequest.value)
		)

		val author = authorsService.getById(id.toLong())

		respond(isCorrect = {
			author != null
		}, onCorrect = {
			call.respond(status = HttpStatusCode.OK, author ?: return@respond)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest,
				Response(HttpStatusCode.BadRequest.value)
			)
		})
	}
}

private fun Route.deleteAuthor(authorsService: AuthorService) {
	delete("/api/v1.0/authors/{id?}") {
		val id = call.parameters["id"] ?: return@delete call.respond(
			status = HttpStatusCode.BadRequest,
			message = Response(HttpStatusCode.BadRequest.value)
		)

		val author = authorsService.deleteById(id.toLong())

		respond(isCorrect = {
			author
		}, onCorrect = {
			call.respond(
				status = HttpStatusCode.NoContent,
				Response(HttpStatusCode.NoContent.value)
			)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest,
				Response(HttpStatusCode.BadRequest.value)
			)
		})
	}
}

private fun Route.updateAuthor(authorsService: AuthorService) {
	put("/api/v1.0/authors") {
		val authorRequestToId = try {
			call.receive<AuthorRequestToId>()
		} catch (e: Exception) {
			null
		}

		val author = authorsService.update(authorRequestToId)

		respond(isCorrect = {
			author != null
		}, onCorrect = {
			call.respond(status = HttpStatusCode.OK, author ?: return@respond)
		}, onIncorrect = {
			call.respond(
				status = HttpStatusCode.BadRequest,
				Response(HttpStatusCode.BadRequest.value)
			)
		})
	}
}