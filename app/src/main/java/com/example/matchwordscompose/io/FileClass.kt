package com.example.wordselect.io

import android.os.Environment
import java.io.*


class FileClass {
    companion object{
        private fun _getBaseDirectory(): String? {
            val pathToExternalStorage= Environment.getExternalStorageDirectory()
            val dir =
                File( "${pathToExternalStorage.absolutePath.toString()}/documents/matchwords")
            if (!dir.exists()) {
                dir.mkdir()
            }
            return dir.getPath()
        }

        fun readFromExternal(fName: String?): String? {
            val dir = File(_getBaseDirectory())
            if (!dir.exists()) {
                dir.mkdir()
                return ""
            }
            val file = File(dir, fName)
            if (!file.exists()) {
                return ""
            }
            val text = StringBuilder()
            try {
                val br = BufferedReader(FileReader(file))
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    text.append(line)
                    text.append('\n')
                }
                br.close()
            } catch (e: IOException) {
                //writeToExternalFile("error.txt", e.getMessage() + "\n" + fName);
                //You'll need to add proper error handling here
            }
            return text.toString()
        }

        fun writetoExternalFile(fName: String?, vData: String?) {
            val appDirectory = File(_getBaseDirectory())
            appDirectory.mkdirs()
            val saveFilePath = File(appDirectory, fName)
            try {
                val fos = FileOutputStream(saveFilePath)
                val OutDataWriter = OutputStreamWriter(fos)
                OutDataWriter.write(vData)
                OutDataWriter.close()
                fos.flush()
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }



    }
}