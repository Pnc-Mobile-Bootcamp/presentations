//
//  LoginModel.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/15/26.
//
import Foundation

struct LoginModel: Codable {
    
    var username: String = ""
    var password: String = ""
    
    enum CodingKeys: String, CodingKey {
        case username = "loginId"
        case password
    }
}
