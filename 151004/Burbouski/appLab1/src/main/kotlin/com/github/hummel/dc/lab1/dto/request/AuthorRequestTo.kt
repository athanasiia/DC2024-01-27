package com.github.hummel.dc.lab1.dto.request

import com.github.hummel.dc.lab1.bean.Author
import kotlinx.serialization.Serializable

@Serializable
class AuthorRequestTo(
	private val login: String, private val password: String, private val firstname: String, private val lastname: String
) {
	fun toBean(id: Long): Author = Author(id, login, password, firstname, lastname)

	init {
		require(login.length in 2..64)
		require(password.length in 8..128)
		require(firstname.length in 2..64)
		require(lastname.length in 2..64)
	}
}