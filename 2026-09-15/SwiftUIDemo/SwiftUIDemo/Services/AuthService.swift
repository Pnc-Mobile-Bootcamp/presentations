//
//  AuthService.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/15/26.
//
import Foundation

class AuthService {
    // use the Singleton pattern
    static let shared = AuthService()
    
    private init() {}
    
    func login(credentials: LoginModel) async throws -> LoginResponse {
        
        let urlString = "https://api.bootcampcentral.com/api/Login"
        
        guard let url = URL(string: urlString) else {
            throw NetworkError.invalidURL
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let encoder = JSONEncoder()
        do {
            request.httpBody = try encoder.encode(credentials)
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
            decoder.useStringDecoderForDate()
            
            return try decoder.decode(LoginResponse.self, from: data)
        }
        catch {
            throw NetworkError.decodingFailed(underlying: error)
        }
        
    }
    
    
    func refreshToken(authToken: String, refreshToken: String) async throws -> LoginResponse {
        
        let urlString = "https://api.bootcampcentral.com/api/Login/refresh"
        
        guard let url = URL(string: urlString) else {
            throw NetworkError.invalidURL
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue("Bearer \(authToken)", forHTTPHeaderField: "Authorization")
        
        let bodyString = "{\"refreshToken\":\"\(refreshToken)\"}"
        request.httpBody = bodyString.data(using: .utf8)
        
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
            decoder.useStringDecoderForDate()
            
            return try decoder.decode(LoginResponse.self, from: data)
        }
        catch {
            throw NetworkError.decodingFailed(underlying: error)
        }
        
        
    }
    
    
}
