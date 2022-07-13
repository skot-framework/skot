package tech.skot.model

import android.util.Base64
import org.junit.Test

class TestEncoding {

    val testCode = "eyJleHAiOjE2NTcwMTk4NzAsImlhdCI6MTY1NzAxODA3MCwiYXV0aF90aW1lIjoxNjU3MDE4MDY3LCJqdGkiOiJhYWQ3YWQ3MS1lNzdlLTRiZWYtODZmMy1iMGJmN2EzYjM0YjUiLCJpc3MiOiJodHRwczovL2F1dGguc2V6YW5lLmNvbS9hdXRoL3JlYWxtcy9jdXN0b21lciIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiIxN2RmZmM4NS1iZTBlLTRiYWMtOTgwZC1lYmRjZDY1OThjNjAiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhcHAtYW5kcm9pZCIsInNlc3Npb25fc3RhdGUiOiIwN2JjZDRkYy02MjgzLTRiZjgtOTA4ZS1mMzQ2MWFlOTlhZjciLCJhY3IiOiIxIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSBvZmZsaW5lX2FjY2VzcyBwaG9uZSIsInNlel9uZXdzbGV0dGVyIjoiMSIsImJpcnRoZGF0ZSI6IjE5ODQtMDgtMTAiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImdlbmRlciI6IjIiLCJzZXpfY2xhc3NldmVydGUiOiIwIiwibmFtZSI6ImNobG_DqSBib2lzIiwicGhvbmVfbnVtYmVyIjoiMDY3OTk4OTEwOCIsInByZWZlcnJlZF91c2VybmFtZSI6ImNobG9lXzA4ODRAeWFob28uZnIiLCJnaXZlbl9uYW1lIjoiY2hsb8OpIiwiZmFtaWx5X25hbWUiOiJib2lzIiwic2V6YW5lSWQiOiIxMzM0NjYiLCJlbWFpbCI6ImNobG9lXzA4ODRAeWFob28uZnIifQ"

    @Test
    fun testDecodeBase64() {



        val decoded = String(Base64.decode(testCode, Base64.URL_SAFE))
        println("------")
        println(decoded)
        println("#####------")
    }
}