package com.example.spinnerlib

data class UserModel(
  var id: Int? = null,
  var name: String? = null
) {
  override fun toString(): String {
    return this.name.toString()
  }
}