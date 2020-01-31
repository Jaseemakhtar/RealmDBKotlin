package com.example.realmdbkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.realm.*
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    private lateinit var mRealm: Realm
    private lateinit var mRealmResults: RealmResults<Person>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val realmConfiguration: RealmConfiguration = RealmConfiguration.Builder()
            .name("test.db")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()

        mRealm = Realm.getInstance(realmConfiguration)
        mRealmResults = mRealm.where<Person>().findAll().sort("id")

        mRealmResults.addChangeListener { personModels, changeSet ->
            clearAll()
            if (changeSet.insertions.isNotEmpty())
                toast("Inserted!")

            if (changeSet.changes.isNotEmpty())
                toast("Updated!")

            if(changeSet.deletions.isNotEmpty())
                toast("Deleted!")
        }



        btnFetch.setOnClickListener {
            if(edtID.text.isEmpty())
                toast("Enter Id!")
            else{
                txtResult.text = ""
                mRealm.executeTransaction { realm ->
                    val result =  mRealmResults.where().equalTo("id", edtID.text.toString().toLong()).findFirst()
                    if(result != null) {
                        txtResult.text = "${result.id} -> ${result.name} : ${result.email}"
                        clearAll()
                    }else
                        toast("Id not present!")
                }
            }
        }


        btnInsert.setOnClickListener {
            if(edtID.text.isEmpty() || edtEmail.text.isEmpty() || edtName.text.isEmpty())
                toast("Fields cannot be empty!")
            else {
                txtResult.text = ""
                mRealm.executeTransaction { realm ->
                    realm.insert(Person(edtID.text.toString().toLong(), edtName.text.toString(), edtEmail.text.toString()))
                }
            }
        }

        btnUpdate.setOnClickListener {
            if(edtID.text.isEmpty())
                toast("Enter Id!")
            else{
                txtResult.text = ""
                mRealm.executeTransaction { realm ->
                    val query: RealmQuery<Person> = realm.where<Person>().equalTo("id", edtID.text.toString().toLong())
                    val p =  query.findFirst()
                    if(query.count() > 0 && p != null){

                        println("${p.id} -> ${p.name} ${p.email}")
                        val person: Person? = Person(p!!.id, p.name, p.email)

                        if(edtName.text.isNotEmpty())
                             person?.name = edtName.text.toString()

                        if(edtEmail.text.isNotEmpty())
                            person?.email = edtEmail.text.toString()

                        realm.insertOrUpdate(person)
                    }else
                        toast("Id not present!")
                }
            }
        }

        btnDelete.setOnClickListener {
            if(edtID.text.isEmpty())
                toast("Enter Id!")
            else{
                txtResult.text = ""
                mRealm.executeTransaction { realm ->
                    val query  = realm.where<Person>().equalTo("id", edtID.text.toString().toLong()).findAll()
                    if(query.count() > 0) {
                        query.deleteAllFromRealm()
                        clearAll()
                    } else
                        toast("Id not present!")
                 }
            }

        }

        btnFetchAll.setOnClickListener {
            txtResult.text = ""
            if(mRealmResults.count() > 0) {
                mRealmResults.forEach {
                    txtResult.text =
                        txtResult.text.toString() + "${it.id} -> ${it.name} : ${it.email}\n"
                }
                clearAll()
            }
            else
                toast("Nothing to fetch!")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mRealm.close()
    }

    private fun toast(msg: String): Unit = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun clearAll() {
        edtName.text.clear()
        edtID.text.clear()
        edtEmail.text.clear()
    }
}
