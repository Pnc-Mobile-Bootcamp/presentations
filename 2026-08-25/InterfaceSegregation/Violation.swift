protocol DataSource {
		func fetchAll() -> [Record]
		func fetchById(_ id: UUID) -> Record?
		func insert(_ record: Record)
		func update(_ record: Record)
		func delete(_ id: UUID)
		func fetchPage(offset: Int, limit: Int) -> [Record]
		func count() -> Int
}

class ReadOnlyRemoteDataSource: DataSource {
		func fetchAll() -> [Record] { 
            /* network call */  
            return [] 
        }
		func fetchById(_ id: UUID) -> Record { 
            /* network call */  
            return nil 
        }
		func insert(_ record: Record) { 
            fatalError("Read-only!") 		// violation
        }
		func update(_ record: Record) { fatalError("Read-only!") }  // violation
		func delete(_ id: UUID) { fatalError("Read-only!") } // violation
		func fetchPage(offset: Int, limit: Int) -> [Record] { return [] }
		func count() -> Int { return 0 }
}

