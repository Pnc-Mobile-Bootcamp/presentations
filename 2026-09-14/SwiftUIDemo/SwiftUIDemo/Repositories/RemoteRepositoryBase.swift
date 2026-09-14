//
//  RemoteRepositoryBase.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/14/26.
//
import Foundation

class RemoteRepositoryBase<Item: Codable> {
    
    func fetchAll(_ urlString: String) async throws -> [Item] {
        
        guard let url = URL(string: urlString) else {
            throw NetworkError.invalidURL
        }
        
        let request = URLRequest(url: url)
        // ultimately we will add authorization to this request
        
        let (data, response) = try await URLSession.shared.data(for: request)
        
        guard let http = response as? HTTPURLResponse else {
            throw NetworkError.badResponse(statusCode: -1)
        }
        
        guard (200...299).contains(http.statusCode) else {
            if http.statusCode == 401 {
                throw NetworkError.unauthorized
            }
            throw NetworkError.badResponse(statusCode: http.statusCode)
        }
        
        do {
            let decoder = JSONDecoder()
            decoder.keyDecodingStrategy = .convertFromSnakeCase
            return try decoder.decode([Item].self, from: data)
        }
        catch {
            throw NetworkError.decodingFailed(underlying: error)
        }
        
    }
    
    func fetchOne(_ urlString: String) async throws -> Item {
        
        guard let url = URL(string: urlString) else {
            throw NetworkError.invalidURL
        }
        
        let request = URLRequest(url: url)
        // ultimately we will add authorization to this request
        
        let (data, response) = try await URLSession.shared.data(for: request)
        
        guard let http = response as? HTTPURLResponse else {
            throw NetworkError.badResponse(statusCode: -1)
        }
        
        guard (200...299).contains(http.statusCode) else {
            if http.statusCode == 401 {
                throw NetworkError.unauthorized
            }
            throw NetworkError.badResponse(statusCode: http.statusCode)
        }
        
        do {
            let decoder = JSONDecoder()
            decoder.keyDecodingStrategy = .convertFromSnakeCase
            return try decoder.decode(Item.self, from: data)
        }
        catch {
            throw NetworkError.decodingFailed(underlying: error)
        }
    }
    
    func post(_ urlString: String, send item: Item) async throws -> Item {
        
        guard let url = URL(string: urlString) else {
            throw NetworkError.invalidURL
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        // ultimately we will add authorization to this request
        
        do {
            request.httpBody = try JSONEncoder().encode(item)
        }
        catch {
            throw NetworkError.encodingFailed(underlying: error)
        }
        
        let (data, response) = try await URLSession.shared.data(for: request)
        
        guard let http = response as? HTTPURLResponse else {
            throw NetworkError.badResponse(statusCode: -1)
        }
        
        guard (200...299).contains(http.statusCode) else {
            if http.statusCode == 401 {
                throw NetworkError.unauthorized
            }
            throw NetworkError.badResponse(statusCode: http.statusCode)
        }
        
        do {
            let decoder = JSONDecoder()
            decoder.keyDecodingStrategy = .convertFromSnakeCase
            return try decoder.decode(Item.self, from: data)
        }
        catch {
            throw NetworkError.decodingFailed(underlying: error)
        }
    }
    
    func put(_ urlString: String, send item: Item) async throws {
        
        guard let url = URL(string: urlString) else {
            throw NetworkError.invalidURL
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "PUT"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        // ultimately we will add authorization to this request
        
        do {
            request.httpBody = try JSONEncoder().encode(item)
        }
        catch {
            throw NetworkError.encodingFailed(underlying: error)
        }
        
        let (_, response) = try await URLSession.shared.data(for: request)
        
        guard let http = response as? HTTPURLResponse else {
            throw NetworkError.badResponse(statusCode: -1)
        }
        
        guard (200...299).contains(http.statusCode) else {
            if http.statusCode == 401 {
                throw NetworkError.unauthorized
            }
            throw NetworkError.badResponse(statusCode: http.statusCode)
        }
        
    }
    
    func del(_ urlString: String) async throws {
        
        guard let url = URL(string: urlString) else {
            throw NetworkError.invalidURL
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "DELETE"
        
        let (_, response) = try await URLSession.shared.data(for: request)
        
        guard let http = response as? HTTPURLResponse else {
            throw NetworkError.badResponse(statusCode: -1)
        }
        
        guard (200...299).contains(http.statusCode) else {
            if http.statusCode == 401 {
                throw NetworkError.unauthorized
            }
            throw NetworkError.badResponse(statusCode: http.statusCode)
        }
    }
    
    
    
}

