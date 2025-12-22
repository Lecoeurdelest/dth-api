/**
 * Shared Kernel Package
 * 
 * <p>This package contains shared infrastructure components that are used across all modules.
 * These components provide cross-cutting concerns and should not contain business logic.
 * 
 * <h3>Package Structure:</h3>
 * <ul>
 *   <li><b>config</b> - Global Spring configuration (OpenAPI, JPA, etc.)</li>
 *   <li><b>security</b> - JWT authentication, Spring Security configuration</li>
 *   <li><b>exception</b> - Global exception handling and custom exceptions</li>
 *   <li><b>response</b> - Unified API response wrapper</li>
 *   <li><b>util</b> - Utility classes (pagination, etc.)</li>
 * </ul>
 * 
 * <h3>Rules:</h3>
 * <ul>
 *   <li>✅ Shared components can be imported by any module</li>
 *   <li>❌ Shared components should NOT import from modules</li>
 *   <li>❌ Shared components should NOT contain business logic</li>
 * </ul>
 */
package com.example.app.shared;

