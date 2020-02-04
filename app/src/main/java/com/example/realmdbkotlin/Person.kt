package com.example.realmdbkotlin

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.jetbrains.annotations.NotNull

open class Person(
    @PrimaryKey
    @NotNull
    var id: Long = 0,
    var name: String = "",
    var email: String = "",
    var department: Department?
): RealmObject()