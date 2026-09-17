//
//  SecureTokenManager.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/15/26.
//
import Foundation
import Security

final class SecureTokenManager {
    static let shared = SecureTokenManager()
    
    private init() {}
    
    private let service = "com.drew-fierst.swift-demo.auth"
    
    func saveToken(_ token: String, key: String) -> Bool {
        // convert the token (String) into binary
        guard let data = token.data(using: .utf8) else { return false }
        
        // create the query
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: key,
            kSecValueData as String: data,
            kSecAttrAccessible as String: kSecAttrAccessibleAfterFirstUnlock
        ]
        
        // delete any existing item (for this key) before saving,
        // to prevent duplicate value conflicts
        SecItemDelete(query as CFDictionary)
        
        let status = SecItemAdd(query as CFDictionary, nil)
        
        return status == errSecSuccess
    }
    
    func getToken(key: String) -> String? {
        
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: key,
            kSecReturnData as String: true,
            kSecMatchLimit as String: kSecMatchLimitOne
        ]
        
        var dataRef: AnyObject?
        let status = SecItemCopyMatching(query as CFDictionary, &dataRef)
        
        if status == errSecSuccess, let data = dataRef as? Data {
            return String(data: data, encoding: .utf8)
        }
        
        return nil
    }
    
    func deleteToken(key: String) {
        
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: key
        ]
        
        SecItemDelete(query as CFDictionary)
    }
    
    
}

