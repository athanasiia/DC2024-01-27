package com.github.hummel.dc.lab1.dto.response

import com.github.hummel.dc.lab1.util.TimeStampSerializer
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
class IssueResponseTo(
	private val id: Long,
	private val authorId: Long,
	private val title: String,
	private val content: String,
	@Serializable(TimeStampSerializer::class) private val created: Timestamp,
	@Serializable(TimeStampSerializer::class) private val modified: Timestamp
)