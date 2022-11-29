package es.unizar.urlshortener.core

import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicInteger

interface FileStore {
    fun init()
    fun store(file:MultipartFile, filename: String)
    fun deleteAll()
}

@Service
class FileStorageImpl: FileStore {
    private var rootLocation = Paths.get("filestorage")

    private var numFiles = AtomicInteger(0)

    override fun init() {
        Files.createDirectory(rootLocation)
    }

    override fun store(file:MultipartFile,filename:String){
        Files.copy(file.inputStream,this.rootLocation.resolve(filename))
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }
}