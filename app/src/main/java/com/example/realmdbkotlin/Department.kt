package com.example.realmdbkotlin

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.jetbrains.annotations.NotNull

open class Department(
    @NotNull
    @PrimaryKey
    var depId: Long,
    var depName: String,
    var depLocation: String,
    var peoples: RealmList<Person>
): RealmObject()