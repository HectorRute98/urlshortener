package es.unizar.urlshortener.core

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile;
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicInteger
import kotlin.io.path.readLines

interface FileStore {
    fun init()
    fun generateName(): String
    fun store(file:MultipartFile, filename: String)
    fun readLines(filename: String): List<String>
    fun overWriteFile(filename: String, lines: List<String>)
    fun loadFile(filename: String): Resource
    fun deleteFile(filename: String)
    fun deleteAll()
}

@Service
class FileStorageImpl: FileStore {
    private var rootLocation = Paths.get("filestorage")

    private var numFiles = AtomicInteger(0)

    override fun init() {
        Files.createDirectory(rootLocation)
    }

    override fun generateName(): String {
        return "temp${numFiles.incrementAndGet()}"
    }

    override fun store(file:MultipartFile,filename:String){
        Files.copy(file.inputStream,this.rootLocation.resolve(filename))
    }

    override fun readLines(filename: String): List<String> {
        return rootLocation.resolve(filename).readLines()
    }

    override fun overWriteFile(filename: String, lines: List<String>) {
        PrintWriter(rootLocation.resolve(filename).toString()).use {
            for (line in lines) {
                it.println(line)
            }
        }
    }

    override fun loadFile(filename: String): Resource {
        val file = rootLocation.resolve(filename)
        val resource = UrlResource(file.toUri())

        if (resource.exists() || resource.isReadable) {
            return resource
        } else {
            println("EL FICHERO NO EXISTE")
            throw IndexOutOfBoundsException() /* TODO */
        }
    }

    override fun deleteFile(filename: String) {
        Files.deleteIfExists(rootLocation.resolve(filename))
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile()) 
    }
}