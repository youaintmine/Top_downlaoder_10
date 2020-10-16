package com.ahmbarish.top10downloaderapps

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.util.Log
import androidx.loader.content.AsyncTaskLoader
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.text.StringBuilder

class MainActivity : AppCompatActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called")

        val dataRecieved = DataFetch()
        dataRecieved.execute("URL goes here")
        Log.d(TAG,"onCreate : Done")
    }

    companion object {
        private class DataFetch : AsyncTask<String, Void, String>() {

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d(DATATAG, "onPost Execute : $result")
            }

            override fun doInBackground(vararg p0: String?): String {
                Log.d(DATATAG, "doInBackground starts with : ${p0[0]}")
                val rssFeed = downloadXML(p0[0])
                 if(rssFeed.isEmpty()) {
                     Log.e(DATATAG,"doInBackground : Error downloading")
                 }
                return rssFeed
            }

            private fun downloadXML(urlPath : String?) : String {
                val xmlResult = StringBuilder()

                try {
                    val Url = URL(urlPath)
                    val connect: HttpURLConnection = Url.openConnection() as HttpURLConnection
                    val response = connect.responseCode
                    Log.d(DATATAG, "downloadXML : The response code $response")

//                    val reader = BufferedReader(InputStreamReader(connect.inputStream))
//
//                    val inputBuffer = CharArray(500)
//                    var charRead = 0
//                    while (charRead >= 0) {
//                        charRead = reader.read(inputBuffer)
//                        if (charRead > 0) {
//                            xmlResult.append(String(inputBuffer, 0, charRead))
//                        }
//                    }
//                    reader.close()
               //Replace the above code
                    val stream = connect.inputStream
                    stream.buffered().reader().use {reader ->
                        xmlResult.append(reader.readText())
                    }

                    Log.d(DATATAG, "Received ${xmlResult.length}")
                    return xmlResult.toString()
//                }catch (e: MalformedURLException) {
//                    Log.e(DATATAG,"downloadXML : Invalid URL ${e.message}")
//                }catch (e:IOException) {
//                    Log.e(DATATAG,"downloadXML : IO Except ${e.message}")
//                }catch (e: SecurityException) {
//                    e.printStackTrace()
//                    Log.e(DATATAG,"Network Permission not available for network ${e.message}")
//                }catch (e : Exception) {
//                    Log.e(DATATAG,"Unknown Errors ${e.message}")
//                }
                }
                  catch(e:Exception) {
                      val errorMessage: String = when(e) {
                          is MalformedURLException -> "downloadXML : Invalid URL ${e.message}"
                          is IOException -> "downloadXML : IO Except ${e.message}"
                          is SecurityException -> {e.printStackTrace()
                              "Network Permission not available for network ${e.message}"
                          }
                          else -> "Unknown Errors ${e.message}"
                      }
                      Log.e(DATATAG,errorMessage)
                  }
                return ""
            }
        }
    }


}