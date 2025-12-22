/**
 * Services Module
 * 
 * <p>This module handles service catalog functionality.
 * 
 * <h3>Module Structure:</h3>
 * <ul>
 *   <li><b>api</b> - REST controllers for service endpoints</li>
 *   <li><b>application</b> - Business logic and public API interfaces</li>
 *   <li><b>domain</b> - Service entities (PRIVATE)</li>
 *   <li><b>repository</b> - JPA repositories (PRIVATE)</li>
 *   <li><b>dto</b> - Request/Response DTOs</li>
 *   <li><b>mapper</b> - MapStruct mappers</li>
 * </ul>
 * 
 * <h3>Public API:</h3>
 * <ul>
 *   <li>{@link com.example.app.services.application.ServiceApplicationService} - Cross-module communication interface</li>
 * </ul>
 * 
 * <h3>Module Rules:</h3>
 * <ul>
 *   <li>✅ Other modules can use ServiceApplicationService interface</li>
 *   <li>❌ Other modules CANNOT import Service entity</li>
 *   <li>❌ Other modules CANNOT import ServiceRepository</li>
 *   <li>✅ Use ServiceDto for data transfer</li>
 * </ul>
 */
package com.example.app.services;

