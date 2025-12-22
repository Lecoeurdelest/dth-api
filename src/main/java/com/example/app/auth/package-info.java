/**
 * Auth Module
 * 
 * <p>This module handles authentication and authorization functionality.
 * 
 * <h3>Module Structure:</h3>
 * <ul>
 *   <li><b>api</b> - REST controllers for authentication endpoints</li>
 *   <li><b>application</b> - Business logic and public API interfaces</li>
 *   <li><b>domain</b> - User and RefreshToken entities (PRIVATE)</li>
 *   <li><b>repository</b> - JPA repositories (PRIVATE)</li>
 *   <li><b>dto</b> - Request/Response DTOs</li>
 *   <li><b>mapper</b> - MapStruct mappers</li>
 * </ul>
 * 
 * <h3>Public API:</h3>
 * <ul>
 *   <li>{@link com.example.app.auth.application.AuthApplicationService} - Cross-module communication interface</li>
 * </ul>
 * 
 * <h3>Module Rules:</h3>
 * <ul>
 *   <li>✅ Other modules can use AuthApplicationService interface</li>
 *   <li>❌ Other modules CANNOT import User or RefreshToken entities</li>
 *   <li>❌ Other modules CANNOT import UserRepository</li>
 *   <li>✅ Use UserDto for data transfer</li>
 * </ul>
 */
package com.example.app.auth;

