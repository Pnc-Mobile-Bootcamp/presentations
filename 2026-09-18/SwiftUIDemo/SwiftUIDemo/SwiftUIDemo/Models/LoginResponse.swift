//
//  LoginResponse.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/15/26.
//
import Foundation

struct LoginResponse: Codable {
    
    var success: Bool
    var userName: String
    var accessToken: String?
    var refreshToken: String?
    var accessExpiry: Date?
    var refreshExpiry: Date?
    
    
    enum CodingKeys: String, CodingKey {
        case success, userName, accessToken, refreshToken
        case accessExpiry = "accessTokenExpiresAtUtc"
        case refreshExpiry = "refreshTokenExpiresAtUtc"
    }
    
}
