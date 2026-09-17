//
//  AuthStatus.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/15/26.
//
import SwiftUI
internal import Combine

class AuthStatus: ObservableObject {
    
    private let authKey = "userAccessToken"
    private let refreshKey = "userRefreshToken"
    
    @Published private(set) var isLoggedIn = false
    
    
    init() {
        let auth = SecureTokenManager.shared.getToken(key: authKey)
        let refresh = SecureTokenManager.shared.getToken(key: refreshKey)
        
        self.isLoggedIn = auth != nil && refresh != nil
    }
    
    
    var authToken: String? {
        return SecureTokenManager.shared.getToken(key: authKey)
    }
    var refreshToken: String? {
        return SecureTokenManager.shared.getToken(key: refreshKey)
    }
    
    
    func updateLoginStatus(success: Bool,
                           authToken: String? = "",
                           refreshToken: String? = "") {
        withAnimation {
            isLoggedIn = success
        }
        
        let storage = SecureTokenManager.shared
        
        if let auth = authToken, !auth.isEmpty {
            let _ = storage.saveToken(auth, key: authKey)
        } else {
            storage.deleteToken(key: authKey)
        }
        
        if let refresh = refreshToken, !refresh.isEmpty {
            let _ = storage.saveToken(refresh, key: refreshKey)
        } else {
            storage.deleteToken(key: refreshKey)
        }
        
        
    }
    
}
