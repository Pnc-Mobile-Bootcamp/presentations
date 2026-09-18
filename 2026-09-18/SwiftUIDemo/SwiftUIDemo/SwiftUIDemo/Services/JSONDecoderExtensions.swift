//
//  JSONDecoderExtensions.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/15/26.
//
import Foundation

extension JSONDecoder {
    
    func useStringDecoderForDate() {
        
        self.dateDecodingStrategy = .custom { decoder in
            
            let container = try decoder.singleValueContainer()
            
            guard let value = try container.decode(String?.self) else {
                return Date.distantPast
            }
            
            let dateFormatter = ISO8601DateFormatter()
            dateFormatter.formatOptions = [.withInternetDateTime, .withFractionalSeconds]
            
            guard let date = dateFormatter.date(from: value) else {
                throw DecodingError.dataCorruptedError(
                    in: container,
                    debugDescription: "Invalid ISO 8601 date: \(value)")
            }
            
            return date
            
        }
        
    }
    
}
