protocol Readable {
    func fetchAll() -> [Record]
	func fetchById(_ id: UUID) -> Record?
}

protocol Writable {
    func insert(_ record: Record)
    func update(_ record: Record)
    func delete(_ id: UUID)
}

protocol Pageable {
    func fetchPage(offset: Int, limit: Int) -> [Record]
    func count() -> Int
}

// compose the individual protocols into the original protocol
// to prevent breaking existing code that used the original
protocol DataSource: Readable, Writable, Pageable {
}

class ReadOnlyRemoteDataSource: Readable, Pageable {
    func fetchAll() -> [Record] { 
        /* network call */  
        return [] 
    }
    func fetchById(_ id: UUID) -> Record { 
        /* network call */  
        return nil 
    }
    func fetchPage(offset: Int, limit: Int) -> [Record] { 
        return [] 
    }
    func count() -> Int { 
        return 0 
    }
}

class LocalCacheDataSource: Readable, Writable {
    . . . 
}

