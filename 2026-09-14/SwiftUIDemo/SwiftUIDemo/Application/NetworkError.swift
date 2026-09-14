//
//  NetworkError.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/14/26.
//

enum NetworkError: Error {
    case invalidURL
    case noConnection
    case badResponse(statusCode: Int)
    case encodingFailed(underlying: Error)
    case decodingFailed(underlying: Error)
    case unauthorized
}

