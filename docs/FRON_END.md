This file is a merged representation of the entire codebase, combined into a single document by Repomix.
The content has been processed where security check has been disabled.

# File Summary

## Purpose
This file contains a packed representation of the entire repository's contents.
It is designed to be easily consumable by AI systems for analysis, code review,
or other automated processes.

## File Format
The content is organized as follows:
1. This summary section
2. Repository information
3. Directory structure
4. Repository files (if enabled)
5. Multiple file entries, each consisting of:
  a. A header with the file path (## File: path/to/file)
  b. The full contents of the file in a code block

## Usage Guidelines
- This file should be treated as read-only. Any changes should be made to the
  original repository files, not this packed version.
- When processing this file, use the file path to distinguish
  between different files in the repository.
- Be aware that this file may contain sensitive information. Handle it with
  the same level of security as you would the original repository.

## Notes
- Some files may have been excluded based on .gitignore rules and Repomix's configuration
- Binary files are not included in this packed representation. Please refer to the Repository Structure section for a complete list of file paths, including binary files
- Files matching patterns in .gitignore are excluded
- Files matching default ignore patterns are excluded
- Security check has been disabled - content may contain sensitive information
- Files are sorted by Git change count (files with more changes are at the bottom)

# Directory Structure
```
app/
  contact/
    page.tsx
  loyalty-points/
    page.tsx
  news/
    page.tsx
  orders/
    [orderId]/
      page.tsx
    page.tsx
  profile/
    page.tsx
  services/
    [serviceId]/
      page.tsx
    page.tsx
  tasks/
    page.tsx
  globals.css
  layout.tsx
  page.tsx
  providers.tsx
guidelines/
  Guidelines.md
lib/
  .gitkeep
  config.ts
modules/
  auth/
    AuthModalRoot.tsx
    index.ts
    LoginModal.tsx
    RegisterModal.tsx
  booking/
    components/
      BookServiceModal.tsx
      PaymentSection.tsx
      WorkerSelectionModal.tsx
    hooks/
      useBooking.ts
    types/
      booking.types.ts
    index.ts
  contact/
    components/
      ContactSection.tsx
    ContactPage.tsx
    index.ts
  home/
    components/
      AboutSection.tsx
      HeroSection.tsx
      ServiceCategories.tsx
      ServiceWorkerSelector.tsx
      TestimonialsSection.tsx
      WorkerSelectionModal.tsx
    HomePage.tsx
    index.ts
  loyalty/
    api/
      loyalty.mock.ts
    hooks/
      useLoyalty.ts
    types/
      loyalty.types.ts
    index.ts
    LoyaltyPointsPage.tsx
  orders/
    api/
      orders.mock.ts
    components/
      CancelOrderModal.tsx
      InvoicePreviewModal.tsx
      OrderItem.tsx
      OrderList.tsx
      OrderPaymentSection.tsx
      OrderStatusFilter.tsx
      OrderSummary.tsx
      ReviewModal.tsx
    hooks/
      useOrders.ts
    types/
      order.types.ts
    index.ts
    OrderDetailPage.tsx
    OrdersPage.tsx
  profile/
    api/
      profile.mock.ts
    hooks/
      useProfile.ts
    types/
      profile.types.ts
    index.ts
    ProfilePage.tsx
  services/
    api/
      services.mock.ts
    components/
      BookServiceModal.tsx
      ProfessionalTeamSection.tsx
      ServiceCategories.tsx
      ServiceGallery.tsx
      ServicePricingTabs.tsx
      ServiceReviews.tsx
      ServiceWorkerSelector.tsx
      WorkerSelectionModal.tsx
    hooks/
      useServiceBooking.ts
    types/
      service.types.ts
    index.ts
    ServiceDetailPage.tsx
    ServicesPage.tsx
  tasks/
    components/
      OrdersTab.tsx
      ProfileTab.tsx
      PromotionsTab.tsx
      TasksTabs.tsx
    types/
      tasks.types.ts
    index.ts
    TasksPage.tsx
  .gitkeep
shared/
  components/
    layout/
      Footer.tsx
      Header.tsx
      index.ts
    .gitkeep
    ChatBox.tsx
  hooks/
    .gitkeep
    index.ts
    useAuth.tsx
    useAuthModal.tsx
    useChat.tsx
    useIsMobile.ts
  types/
    .gitkeep
    index.ts
  utils/
    .gitkeep
    cn.ts
    index.ts
  .gitkeep
src/
  app/
    components/
      figma/
        ImageWithFallback.tsx
      ui/
        accordion.tsx
        alert-dialog.tsx
        alert.tsx
        aspect-ratio.tsx
        avatar.tsx
        badge.tsx
        breadcrumb.tsx
        button.tsx
        calendar.tsx
        card.tsx
        carousel.tsx
        chart.tsx
        checkbox.tsx
        collapsible.tsx
        command.tsx
        context-menu.tsx
        dialog.tsx
        drawer.tsx
        dropdown-menu.tsx
        form.tsx
        hover-card.tsx
        input-otp.tsx
        input.tsx
        label.tsx
        menubar.tsx
        navigation-menu.tsx
        pagination.tsx
        popover.tsx
        progress.tsx
        radio-group.tsx
        resizable.tsx
        scroll-area.tsx
        select.tsx
        separator.tsx
        sheet.tsx
        sidebar.tsx
        skeleton.tsx
        slider.tsx
        sonner.tsx
        switch.tsx
        table.tsx
        tabs.tsx
        textarea.tsx
        toggle-group.tsx
        toggle.tsx
        tooltip.tsx
        use-mobile.ts
        utils.ts
      AboutSection.tsx
      BookServiceModal.tsx
      ChatBox.tsx
      ContactSection.tsx
      FloatingContact.tsx
      Footer.tsx
      Header.tsx
      HeroSection.tsx
      InvoicePreviewModal.tsx
      OrderCard.tsx
      OrderDetailModal.tsx
      ProfessionalTeamSection.tsx
      ReviewList.tsx
      ReviewModal.tsx
      ServiceCategories.tsx
      ServiceDetailModal.tsx
      ServiceGrid.tsx
      ServicePricingTabs.tsx
      ServicesSection.tsx
      ServiceWorkerSelector.tsx
      TestimonialsSection.tsx
      WorkerSelectionModal.tsx
    contexts/
      ChatContext.tsx
      UserContext.tsx
    data/
      ordersData.ts
      servicesData.ts
    pages/
      ContactPage.tsx
      HomePage.tsx
      LoginPage.tsx
      LoyaltyPointsPage.tsx
      NewsPage.tsx
      OrderHistoryPage.tsx
      ProfilePage.tsx
      RegisterPage.tsx
      ServiceDetailPage.tsx
      ServicesPage.tsx
      TasksPage.tsx
    App.tsx
  styles/
    index.css
    tailwind.css
    theme.css
  main.tsx
.gitignore
ARCHITECTURE_PROPOSAL.md
ATTRIBUTIONS.md
DESIGN_PAGES_MAP.md
FEATURE_MODULES_INVENTORY.md
index.html
MODULE_SCOPE_LOCK.md
next-env.d.ts
next.config.js
ORDER_HISTORY_README.md
package.json
PHASE1_COMPLETE.md
PHASE2_COMPLETE.md
PHASE3_COMPLETE.md
postcss.config.mjs
ROUTE_DECISION_TABLE.md
tailwind.config.ts
tsconfig.json
vite.config.ts
```

## File: .gitignore
````
# =========================
# Node & Package Managers
# =========================
node_modules/
npm-debug.log*
yarn-debug.log*
yarn-error.log*
pnpm-debug.log*
.pnpm-store/

# =========================
# Next.js
# =========================
.next/
out/
.vercel/

# =========================
# Build outputs
# =========================
dist/
build/

# =========================
# Environment variables
# =========================
.env
.env.local
.env.development
.env.test
.env.production\

# =========================
# Logs
# =========================
logs/
*.log

# =========================
# OS files
# =========================
.DS_Store
Thumbs.db

# =========================
# Editor / IDE
# =========================
.vscode/*
!.vscode/extensions.json
!.vscode/settings.json
!.vscode/launch.json

.idea/
*.suo
*.ntvs*
*.njsproj
*.sln

# =========================
# TypeScript
# =========================
*.tsbuildinfo

# =========================
# Coverage / Tests
# =========================
coverage/
*.lcov

# =========================
# Cache
# =========================
.cache/
.eslintcache
.stylelintcache

# =========================
# Tailwind / PostCSS
# =========================
.tailwindcss-cache/

# =========================
# Cursor / AI tools
# =========================
.cursor/
.cursor-rules/
.cursorignore

# =========================
# Temporary files
# =========================
tmp/
temp/
*.tmp
*.swp
*.bak

# =========================
# Markdown build artifacts
# =========================
*.md.html

# =========================
# Misc
# =========================
*.zip
*.tar.gz


#keys
dth.pem
````

## File: ARCHITECTURE_PROPOSAL.md
````markdown
# Next.js Migration - Modular Monolithic Architecture Proposal

## Current Codebase Analysis

### Current Structure (React Router + Vite)
```
src/
├── app/
│   ├── App.tsx (Router setup)
│   ├── components/ (Mixed: feature + shared)
│   ├── contexts/ (UserContext, ChatContext)
│   ├── data/ (Mock data)
│   └── pages/ (11 pages)
├── assets/ (Images)
└── styles/ (CSS files)
```

### Identified Features/Modules

1. **Home** - Landing page with hero, about, services preview, testimonials
2. **Auth** - Login, Register pages
3. **Services** - Services listing, service detail, booking modals
4. **News** - News/articles listing and detail view
5. **Contact** - Contact form and information
6. **Orders** - Order history, order details, invoices
7. **Profile** - User profile management
8. **Tasks** - Dashboard/tasks page (multi-tab: profile, orders, promotions)
9. **Loyalty** - Loyalty points page
10. **Chat** - Chat functionality (context + component)
11. **Layout** - Header, Footer, FloatingContact (shared layout components)

### Current Routing
- `/` - HomePage
- `/services` - ServicesPage
- `/services/:serviceId` - ServiceDetailPage
- `/news` - NewsPage
- `/contact` - ContactPage
- `/tasks` - TasksPage
- `/login` - LoginPage
- `/register` - RegisterPage
- `/profile` - ProfilePage
- `/orders` - OrderHistoryPage
- `/loyalty-points` - LoyaltyPointsPage

---

## Proposed Next.js Modular Monolithic Architecture

### Target Structure

```
project-root/
├── app/                          # Next.js App Router (routing only)
│   ├── layout.tsx               # Root layout (Header, Footer, providers)
│   ├── page.tsx                 # Home page
│   ├── services/
│   │   ├── page.tsx            # Services listing
│   │   └── [serviceId]/
│   │       └── page.tsx        # Service detail
│   ├── news/
│   │   ├── page.tsx            # News listing
│   │   └── [articleId]/
│   │       └── page.tsx        # Article detail (if needed)
│   ├── contact/
│   │   └── page.tsx            # Contact page
│   ├── tasks/
│   │   └── page.tsx            # Tasks/Dashboard page
│   ├── login/
│   │   └── page.tsx            # Login page
│   ├── register/
│   │   └── page.tsx            # Register page
│   ├── profile/
│   │   └── page.tsx            # Profile page
│   ├── orders/
│   │   └── page.tsx            # Order history
│   └── loyalty-points/
│       └── page.tsx            # Loyalty points
│
├── modules/                      # Feature modules (strict boundaries)
│   ├── home/
│   │   ├── components/         # Home-specific components
│   │   │   ├── HeroSection.tsx
│   │   │   ├── AboutSection.tsx
│   │   │   ├── ServicesSection.tsx
│   │   │   ├── TestimonialsSection.tsx
│   │   │   ├── ProfessionalTeamSection.tsx
│   │   │   ├── ServiceCategories.tsx
│   │   │   └── ServiceWorkerSelector.tsx
│   │   ├── hooks/              # Home-specific hooks
│   │   └── index.ts            # Public API
│   │
│   ├── auth/
│   │   ├── components/
│   │   │   ├── LoginForm.tsx
│   │   │   └── RegisterForm.tsx
│   │   ├── hooks/
│   │   │   └── useAuth.ts      # Auth logic (from UserContext)
│   │   ├── types/
│   │   │   └── user.types.ts
│   │   └── index.ts
│   │
│   ├── services/
│   │   ├── components/
│   │   │   ├── ServiceGrid.tsx
│   │   │   ├── ServiceDetailView.tsx
│   │   │   ├── ServicePricingTabs.tsx
│   │   │   ├── BookServiceModal.tsx
│   │   │   ├── ServiceDetailModal.tsx
│   │   │   └── ServiceWorkerSelector.tsx
│   │   ├── hooks/
│   │   │   ├── useServices.ts
│   │   │   └── useServiceDetail.ts
│   │   ├── api/
│   │   │   └── services.api.ts  # Move servicesData here
│   │   ├── types/
│   │   │   └── service.types.ts
│   │   └── index.ts
│   │
│   ├── news/
│   │   ├── components/
│   │   │   └── NewsArticleCard.tsx (if extracted)
│   │   ├── hooks/
│   │   │   └── useNews.ts
│   │   ├── api/
│   │   │   └── news.api.ts     # Mock news data
│   │   ├── types/
│   │   │   └── news.types.ts
│   │   └── index.ts
│   │
│   ├── contact/
│   │   ├── components/
│   │   │   └── ContactSection.tsx
│   │   ├── hooks/
│   │   │   └── useContact.ts
│   │   └── index.ts
│   │
│   ├── orders/
│   │   ├── components/
│   │   │   ├── OrderCard.tsx
│   │   │   ├── OrderDetailModal.tsx
│   │   │   └── InvoicePreviewModal.tsx
│   │   ├── hooks/
│   │   │   └── useOrders.ts
│   │   ├── api/
│   │   │   └── orders.api.ts   # Move ordersData here
│   │   ├── types/
│   │   │   └── order.types.ts
│   │   └── index.ts
│   │
│   ├── profile/
│   │   ├── components/
│   │   │   └── ProfileForm.tsx (if extracted)
│   │   ├── hooks/
│   │   │   └── useProfile.ts
│   │   └── index.ts
│   │
│   ├── tasks/
│   │   ├── components/
│   │   │   └── TasksDashboard.tsx (if extracted)
│   │   ├── hooks/
│   │   │   └── useTasks.ts
│   │   └── index.ts
│   │
│   ├── loyalty/
│   │   ├── components/
│   │   │   └── LoyaltyPointsView.tsx (if extracted)
│   │   ├── hooks/
│   │   │   └── useLoyalty.ts
│   │   └── index.ts
│   │
│   └── chat/
│       ├── components/
│       │   └── ChatBox.tsx
│       ├── hooks/
│       │   └── useChat.ts      # From ChatContext
│       ├── types/
│       │   └── chat.types.ts
│       └── index.ts
│
├── shared/                       # Shared, domain-agnostic code
│   ├── components/
│   │   ├── layout/
│   │   │   ├── Header.tsx
│   │   │   ├── Footer.tsx
│   │   │   └── FloatingContact.tsx
│   │   ├── ui/                  # shadcn/ui components (keep as-is)
│   │   │   └── [all ui components]
│   │   └── ImageWithFallback.tsx
│   ├── hooks/
│   │   └── [shared hooks]
│   ├── utils/
│   │   └── [utility functions]
│   └── types/
│       └── [shared types]
│
├── lib/                          # Application-level config
│   ├── config.ts
│   └── http-client.ts            # HTTP abstraction (if needed)
│
├── public/
│   └── images/                   # Static assets
│
└── styles/                       # Global styles
    ├── globals.css
    └── [other css files]
```

---

## Migration Strategy

### Phase 1: Next.js Setup & Configuration
1. Initialize Next.js project with TypeScript
2. Configure Tailwind CSS
3. Set up path aliases
4. Migrate global styles
5. Create root layout structure

### Phase 2: Shared Infrastructure
1. Move `shared/components/ui/` (shadcn components)
2. Move `shared/components/layout/` (Header, Footer, FloatingContact)
3. Move `shared/components/ImageWithFallback.tsx`
4. Set up providers in root layout

### Phase 3: Module Extraction (Feature by Feature)
1. **Home Module** - Extract home components and hooks
2. **Auth Module** - Extract auth logic from UserContext
3. **Services Module** - Extract services components, data, types
4. **News Module** - Extract news components and data
5. **Contact Module** - Extract contact components
6. **Orders Module** - Extract orders components and data
7. **Profile Module** - Extract profile components
8. **Tasks Module** - Extract tasks components
9. **Loyalty Module** - Extract loyalty components
10. **Chat Module** - Extract chat from context to module

### Phase 4: Route Migration
1. Convert React Router routes to Next.js App Router
2. Create page.tsx files in app/ directory
3. Update all imports to use module boundaries
4. Test all routes

### Phase 5: Finalization
1. Remove old React Router code
2. Clean up unused files
3. Verify UI preservation
4. Test build and runtime

---

## Module Boundary Rules

### ✅ Allowed
- Modules can import from `shared/`
- Modules can import from `lib/`
- App routes can import from modules via `modules/{module}/index.ts`
- Modules can have internal imports within themselves

### ❌ Forbidden
- Direct imports between modules (e.g., `modules/services` → `modules/orders`)
- App routes importing module internals (e.g., `modules/services/components/...`)
- Shared importing from modules
- Deep imports (always use module's `index.ts`)

---

## Key Decisions

1. **Context Migration**: Convert React Contexts to module hooks
   - `UserContext` → `modules/auth/hooks/useAuth.ts`
   - `ChatContext` → `modules/chat/hooks/useChat.ts`

2. **Data Migration**: Move mock data to module `api/` folders
   - `data/servicesData.ts` → `modules/services/api/services.api.ts`
   - `data/ordersData.ts` → `modules/orders/api/orders.api.ts`

3. **Component Organization**:
   - Feature-specific components → Module `components/`
   - Layout components → `shared/components/layout/`
   - Generic UI components → `shared/components/ui/`

4. **Type Safety**: Extract types to module `types/` folders

5. **UI Preservation**: 
   - Keep all JSX structure identical
   - Keep all Tailwind classes identical
   - Keep all component hierarchies identical
   - Only refactor internal organization

---

## Next Steps

1. Review and approve this architecture
2. Begin Phase 1: Next.js setup
3. Proceed incrementally through each phase
4. Test after each module migration
````

## File: ATTRIBUTIONS.md
````markdown
This Figma Make file includes components from [shadcn/ui](https://ui.shadcn.com/) used under [MIT license](https://github.com/shadcn-ui/ui/blob/main/LICENSE.md).

This Figma Make file includes photos from [Unsplash](https://unsplash.com) used under [license](https://unsplash.com/license).
````

## File: DESIGN_PAGES_MAP.md
````markdown
# Design Pages Map

## Analysis Date
Based on original project scope: `repomix-output-Home Page Design.zip.md`

---

## Full Pages (11)

### 1. HomePage
- **Route:** `/`
- **Type:** Full Page
- **Layout:** Standard (Header + Footer)
- **Components:**
  - HeroSection
  - AboutSection
  - ServiceCategories
  - ServiceWorkerSelector
  - TestimonialsSection
- **UI States:** None
- **Notes:** Landing page with multiple sections

---

### 2. ServicesPage
- **Route:** `/services`
- **Type:** Full Page
- **Layout:** Standard (Header + Footer)
- **Components:**
  - ProfessionalTeamSection
  - ServiceCategories
  - ServiceWorkerSelector
- **UI States:** None
- **Notes:** Services listing page

---

### 3. ServiceDetailPage
- **Route:** `/services/:serviceId`
- **Type:** Full Page
- **Layout:** Standard (Header + Footer)
- **Components:**
  - Service header image
  - Service title and rating
  - ServicePricingTabs
  - Service commitments
  - Service images gallery
  - Service video (if available)
  - Customer reviews section
- **UI States:**
  - BookServiceModal (triggered by "Đặt dịch vụ" button)
  - Expandable categories (accordion-style)
- **Notes:** Dynamic route based on serviceId

---

### 4. NewsPage
- **Route:** `/news`
- **Type:** Full Page
- **Layout:** Standard (Header + Footer)
- **Components:**
  - Featured article section
  - News articles grid/list
- **UI States:** None
- **Notes:** News/articles listing page

---

### 5. ContactPage
- **Route:** `/contact`
- **Type:** Full Page
- **Layout:** Standard (Header + Footer)
- **Components:**
  - ContactSection
- **UI States:**
  - ChatBox (floating, always available)
- **Notes:** Contact information and form

---

### 6. TasksPage
- **Route:** `/tasks`
- **Type:** Full Page
- **Layout:** Standard (Header + Footer)
- **Components:**
  - Tab navigation (profile, orders, promotions)
  - Profile tab content
  - Orders tab content
  - Promotions tab content
- **UI States:**
  - Tab switching: 'profile' | 'orders' | 'promotions'
  - Edit mode toggle (for profile)
  - OrderDetailModal (triggered from orders tab)
- **Notes:** Dashboard-style page with tabbed interface

---

### 7. LoginPage
- **Route:** `/login`
- **Type:** Full Page
- **Layout:** Full-screen (no Header/Footer visible)
- **Components:**
  - Logo header
  - Social login buttons
  - Login method tabs
  - Login form
- **UI States:**
  - Login method tabs: 'username' | 'email' | 'phone'
  - Password visibility toggle
- **Notes:** Standalone authentication page

---

### 8. RegisterPage
- **Route:** `/register`
- **Type:** Full Page
- **Layout:** Full-screen (no Header/Footer visible)
- **Components:**
  - Logo header
  - Social registration buttons
  - Multi-section registration form
- **UI States:**
  - Password visibility toggle
  - Confirm password visibility toggle
  - Verification method radio: 'email' | 'phone'
- **Notes:** Standalone registration page

---

### 9. ProfilePage
- **Route:** `/profile`
- **Type:** Full Page
- **Layout:** Standard (Header + Footer)
- **Components:**
  - Profile header with avatar
  - Loyalty points card (clickable, navigates to /loyalty-points)
  - Profile information form
- **UI States:**
  - Edit mode toggle (view/edit)
- **Notes:** User profile management, requires authentication

---

### 10. OrderHistoryPage
- **Route:** `/orders`
- **Type:** Full Page
- **Layout:** Standard (Header + Footer)
- **Components:**
  - Page header
  - Review guide banner (conditional)
  - Status filter buttons
  - Statistics cards
  - Order cards grid
- **UI States:**
  - Status filter: 'all' | 'pending' | 'processing' | 'repairing' | 'completed' | 'cancelled'
  - OrderDetailModal (triggered from order card)
  - ReviewModal (triggered from order detail or review button)
  - InvoicePreviewModal (triggered from order detail)
  - Review guide visibility toggle
- **Notes:** Order management page, requires authentication

---

### 11. LoyaltyPointsPage
- **Route:** `/loyalty-points`
- **Type:** Full Page
- **Layout:** Standard (Header + Footer)
- **Components:**
  - Points header with total points
  - Current tier badge
  - Points history list
  - Promotion tiers table
  - Next tier progress
- **UI States:** None
- **Notes:** Loyalty program page, requires authentication

---

## UI States (Not Full Pages)

### Modals

#### BookServiceModal
- **Type:** Modal/Dialog
- **Trigger:** "Đặt dịch vụ" button on ServiceDetailPage
- **Purpose:** Service booking form
- **Parent Page:** ServiceDetailPage

#### ServiceDetailModal
- **Type:** Modal/Dialog
- **Trigger:** Service card click (if used)
- **Purpose:** Quick service preview
- **Parent Page:** ServicesPage (if used)

#### OrderDetailModal
- **Type:** Modal/Dialog
- **Trigger:** "Xem chi tiết" button on OrderCard
- **Purpose:** Full order details view
- **Parent Page:** OrderHistoryPage, TasksPage (orders tab)
- **Nested States:**
  - InvoicePreviewModal (triggered from order detail)
  - ReviewModal (triggered from order detail)

#### InvoicePreviewModal
- **Type:** Modal/Dialog
- **Trigger:** "Xem hóa đơn" button in OrderDetailModal
- **Purpose:** Invoice PDF preview
- **Parent Page:** OrderDetailModal (nested)

#### ReviewModal
- **Type:** Modal/Dialog
- **Trigger:** "Đánh giá dịch vụ" button on OrderCard or OrderDetailModal
- **Purpose:** Submit service review with rating, text, and images
- **Parent Page:** OrderHistoryPage, OrderDetailModal

#### WorkerSelectionModal
- **Type:** Modal/Dialog
- **Trigger:** Service type card click on ServiceWorkerSelector
- **Purpose:** Select available worker for service
- **Parent Page:** HomePage, ServicesPage

---

### Floating Components

#### ChatBox
- **Type:** Floating component
- **Trigger:** Chat icon button (always visible)
- **Purpose:** Customer support chat
- **Parent Page:** All pages (global)
- **Notes:** Uses ChatContext for state management

#### FloatingContact
- **Type:** Floating component
- **Trigger:** Always visible
- **Purpose:** Quick contact buttons (call, consult)
- **Parent Page:** All pages (global)

---

### Tab States

#### TasksPage Tabs
- **Type:** Tab navigation
- **States:** 'profile' | 'orders' | 'promotions'
- **Purpose:** Switch between profile management, order history, and promotions
- **Parent Page:** TasksPage

#### LoginPage Login Method Tabs
- **Type:** Tab navigation
- **States:** 'username' | 'email' | 'phone'
- **Purpose:** Switch login input method
- **Parent Page:** LoginPage

---

### Toggle States

#### Password Visibility Toggle
- **Type:** Toggle
- **States:** show/hide password
- **Parent Pages:** LoginPage, RegisterPage

#### Profile Edit Mode Toggle
- **Type:** Toggle
- **States:** view/edit mode
- **Parent Page:** ProfilePage, TasksPage (profile tab)

#### Review Guide Visibility Toggle
- **Type:** Toggle
- **States:** show/hide guide banner
- **Parent Page:** OrderHistoryPage

---

## Shared Layout Components

### Header
- **Type:** Layout component
- **Usage:** All pages except LoginPage, RegisterPage
- **Features:**
  - Logo
  - Navigation links
  - User menu dropdown (conditional on auth state)
  - Mobile menu toggle

### Footer
- **Type:** Layout component
- **Usage:** All pages except LoginPage, RegisterPage
- **Features:**
  - Company info
  - Quick links
  - Contact information
  - Social media links

---

## Summary

- **Total Full Pages:** 11
- **Total Modals:** 6
- **Total Floating Components:** 2
- **Total Tab Interfaces:** 2
- **Total Toggle States:** 3

**Pages with Standard Layout:** 9 (Home, Services, ServiceDetail, News, Contact, Tasks, Profile, Orders, Loyalty)
**Pages with Full-Screen Layout:** 2 (Login, Register)
````

## File: FEATURE_MODULES_INVENTORY.md
````markdown
# Feature Modules Inventory

## Analysis Date
Based on original project scope from `repomix-output-Home Page Design.zip.md` and current Next.js implementation.

---

## Required Feature Modules

### 1. **Home Module**
- **Status:** ✅ **IMPLEMENTED**
- **Related Pages/Screens:**
  - HomePage (landing page)
- **Expected Routes:**
  - `/` → HomePage
- **Current Implementation:**
  - ✅ Module created: `modules/home/`
  - ✅ Entry point: `modules/home/HomePage.tsx`
  - ✅ Components migrated: HeroSection, AboutSection, ServiceCategories, ServiceWorkerSelector, TestimonialsSection
  - ✅ Route: `app/page.tsx` → renders `<HomePage />`
- **Notes:** Complete and approved as reference module

---

### 2. **Auth Module**
- **Status:** ✅ **IMPLEMENTED**
- **Related Pages/Screens:**
  - LoginPage
  - RegisterPage
- **Expected Routes:**
  - `/auth/login` → LoginPage
  - `/auth/register` → RegisterPage
- **Current Implementation:**
  - ✅ Module created: `modules/auth/`
  - ✅ Entry points: `modules/auth/LoginPage.tsx`, `modules/auth/RegisterPage.tsx`
  - ✅ Routes: `app/auth/login/page.tsx`, `app/auth/register/page.tsx`
  - ✅ Uses shared `useAuth` hook
- **Notes:** Complete, follows Home module structure

---

### 3. **Services Module**
- **Status:** ❌ **MISSING**
- **Related Pages/Screens:**
  - ServicesPage (services listing)
  - ServiceDetailPage (service detail view)
- **Expected Routes:**
  - `/services` → ServicesPage
  - `/services/[serviceId]` → ServiceDetailPage
- **Current Implementation:**
  - ⚠️ Route placeholders exist: `app/services/page.tsx`, `app/services/[serviceId]/page.tsx`
  - ❌ No module created: `modules/services/` does not exist
  - ❌ Components not migrated from `src/app/pages/ServicesPage.tsx`, `src/app/pages/ServiceDetailPage.tsx`
- **Original Components (to be migrated):**
  - ServiceGrid
  - ServicePricingTabs
  - ServiceDetailModal
  - BookServiceModal
  - ProfessionalTeamSection (used in ServicesPage)
  - ServiceCategories (used in ServicesPage)
  - ServiceWorkerSelector (used in ServicesPage)

---

### 4. **News Module**
- **Status:** ❌ **MISSING**
- **Related Pages/Screens:**
  - NewsPage (news/articles listing)
- **Expected Routes:**
  - `/news` → NewsPage
- **Current Implementation:**
  - ⚠️ Route placeholder exists: `app/news/page.tsx`
  - ❌ No module created: `modules/news/` does not exist
  - ❌ Component not migrated from `src/app/pages/NewsPage.tsx`
- **Original Components (to be migrated):**
  - NewsPage component with article listing

---

### 5. **Contact Module**
- **Status:** ❌ **MISSING**
- **Related Pages/Screens:**
  - ContactPage
- **Expected Routes:**
  - `/contact` → ContactPage
- **Current Implementation:**
  - ⚠️ Route placeholder exists: `app/contact/page.tsx`
  - ❌ No module created: `modules/contact/` does not exist
  - ❌ Component not migrated from `src/app/pages/ContactPage.tsx`
- **Original Components (to be migrated):**
  - ContactSection (used in ContactPage)
  - ChatBox (shared component, used in ContactPage)

---

### 6. **Orders Module**
- **Status:** ❌ **MISSING**
- **Related Pages/Screens:**
  - OrderHistoryPage (order listing and management)
- **Expected Routes:**
  - `/orders` → OrderHistoryPage
- **Current Implementation:**
  - ⚠️ Route placeholder exists: `app/orders/page.tsx`
  - ❌ No module created: `modules/orders/` does not exist
  - ❌ Component not migrated from `src/app/pages/OrderHistoryPage.tsx`
- **Original Components (to be migrated):**
  - OrderCard
  - OrderDetailModal
  - InvoicePreviewModal
  - ReviewModal
  - ReviewList

---

### 7. **Profile Module**
- **Status:** ❌ **MISSING**
- **Related Pages/Screens:**
  - ProfilePage (user profile management)
- **Expected Routes:**
  - `/profile` → ProfilePage
- **Current Implementation:**
  - ⚠️ Route placeholder exists: `app/profile/page.tsx`
  - ❌ No module created: `modules/profile/` does not exist
  - ❌ Component not migrated from `src/app/pages/ProfilePage.tsx`
- **Original Components (to be migrated):**
  - ProfilePage component with user info form

---

### 8. **Tasks Module**
- **Status:** ❌ **MISSING**
- **Related Pages/Screens:**
  - TasksPage (dashboard with tabs: profile, orders, promotions)
- **Expected Routes:**
  - `/tasks` → TasksPage
- **Current Implementation:**
  - ⚠️ Route placeholder exists: `app/tasks/page.tsx`
  - ❌ No module created: `modules/tasks/` does not exist
  - ❌ Component not migrated from `src/app/pages/TasksPage.tsx`
- **Original Components (to be migrated):**
  - TasksPage component with tab navigation (profile, orders, promotions)

---

### 9. **Loyalty Module**
- **Status:** ❌ **MISSING**
- **Related Pages/Screens:**
  - LoyaltyPointsPage (loyalty points and promotions)
- **Expected Routes:**
  - `/loyalty-points` → LoyaltyPointsPage
- **Current Implementation:**
  - ⚠️ Route placeholder exists: `app/loyalty-points/page.tsx`
  - ❌ No module created: `modules/loyalty/` does not exist
  - ❌ Component not migrated from `src/app/pages/LoyaltyPointsPage.tsx`
- **Original Components (to be migrated):**
  - LoyaltyPointsPage component with points display and promotion tiers

---

## Shared Components (Already Migrated)

### Layout Components
- **Status:** ✅ **IMPLEMENTED**
- **Location:** `shared/components/layout/`
- **Components:**
  - ✅ Header (presentational, no auth logic)
  - ✅ Footer (presentational)
- **Usage:** Composed in `app/layout.tsx`

### Other Shared Components
- **Status:** ⚠️ **PARTIALLY MIGRATED**
- **Components:**
  - ✅ FloatingContact (exists in `src/app/components/FloatingContact.tsx` - not yet migrated to shared)
  - ✅ ChatBox (exists in `src/app/components/ChatBox.tsx` - not yet migrated to shared)
  - ✅ ImageWithFallback (exists in `src/app/components/figma/ImageWithFallback.tsx` - used by modules)

---

## Summary

### Implemented Modules: 2/9
1. ✅ Home Module
2. ✅ Auth Module

### Missing Modules: 7/9
1. ❌ Services Module
2. ❌ News Module
3. ❌ Contact Module
4. ❌ Orders Module
5. ❌ Profile Module
6. ❌ Tasks Module
7. ❌ Loyalty Module

### Route Status
- ✅ All routes have placeholder pages in `app/` directory
- ❌ Only 2 modules have actual implementations (Home, Auth)
- ⚠️ 7 routes are placeholders waiting for module extraction

---

## Next Steps

1. **Services Module** - Extract ServicesPage and ServiceDetailPage
2. **News Module** - Extract NewsPage
3. **Contact Module** - Extract ContactPage
4. **Orders Module** - Extract OrderHistoryPage
5. **Profile Module** - Extract ProfilePage
6. **Tasks Module** - Extract TasksPage
7. **Loyalty Module** - Extract LoyaltyPointsPage

All modules should follow the Home module structure:
- Entry point at module root (e.g., `ServicesPage.tsx`)
- Internal components in `components/` directory
- Public API via `index.ts`
- No deep imports outside the module
````

## File: index.html
````html
<!DOCTYPE html>
  <html lang="en">
    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <title>Home Page Design</title>
    </head>

    <body>
      <div id="root"></div>
      <script type="module" src="/src/main.tsx"></script>
    </body>
  </html>
````

## File: MODULE_SCOPE_LOCK.md
````markdown
# Module Scope Lock

## Analysis Date
Based on original project scope: `repomix-output-Home Page Design.zip.md`

**Purpose:** Define strict boundaries for each feature module based on design intent.

---

## Module Definitions

### 1. Home Module
**Scope:** Landing page and home-specific content

**Screens:**
- HomePage (full page)

**Routes:**
- `/` → HomePage

**Components:**
- HeroSection
- AboutSection
- ServiceCategories
- ServiceWorkerSelector
- TestimonialsSection
- WorkerSelectionModal (triggered from ServiceWorkerSelector)

**Shared Dependencies:**
- Header (shared layout)
- Footer (shared layout)
- FloatingContact (shared global)
- ChatBox (shared global)
- useChat hook (shared)

**Boundaries:**
- ✅ Can link to `/services`, `/news`, `/contact`
- ❌ Cannot contain service detail logic
- ❌ Cannot contain order management
- ❌ Cannot contain authentication logic

**Notes:** WorkerSelectionModal is part of Home module as it's triggered from ServiceWorkerSelector on the home page.

---

### 2. Services Module
**Scope:** Service listing and service detail pages

**Screens:**
- ServicesPage (full page)
- ServiceDetailPage (full page)

**Routes:**
- `/services` → ServicesPage
- `/services/:serviceId` → ServiceDetailPage

**Components:**
- ServiceGrid (if used)
- ServiceCategories
- ServiceWorkerSelector
- ProfessionalTeamSection
- ServicePricingTabs
- BookServiceModal (triggered from ServiceDetailPage)
- WorkerSelectionModal (triggered from ServiceWorkerSelector)

**Shared Dependencies:**
- Header (shared layout)
- Footer (shared layout)
- FloatingContact (shared global)
- ChatBox (shared global)
- ImageWithFallback (shared UI)

**Boundaries:**
- ✅ Can display service information
- ✅ Can trigger booking flow (modal)
- ❌ Cannot contain order management
- ❌ Cannot contain authentication logic
- ❌ Cannot contain user profile logic

**Notes:** BookServiceModal is part of Services module as it's triggered from ServiceDetailPage.

---

### 3. News Module
**Scope:** News and articles listing

**Screens:**
- NewsPage (full page)

**Routes:**
- `/news` → NewsPage

**Components:**
- News listing components
- Featured article component

**Shared Dependencies:**
- Header (shared layout)
- Footer (shared layout)
- FloatingContact (shared global)
- ChatBox (shared global)
- ImageWithFallback (shared UI)

**Boundaries:**
- ✅ Can display news articles
- ❌ Cannot contain service logic
- ❌ Cannot contain order logic
- ❌ Cannot contain authentication logic

**Notes:** Simple content module, no complex interactions.

---

### 4. Contact Module
**Scope:** Contact information and communication

**Screens:**
- ContactPage (full page)

**Routes:**
- `/contact` → ContactPage

**Components:**
- ContactSection

**Shared Dependencies:**
- Header (shared layout)
- Footer (shared layout)
- FloatingContact (shared global)
- ChatBox (shared global - used here)

**Boundaries:**
- ✅ Can display contact information
- ✅ Can trigger chat (via ChatBox)
- ❌ Cannot contain service booking
- ❌ Cannot contain order management
- ❌ Cannot contain authentication logic

**Notes:** ChatBox is a shared global component, but ContactPage is where it's primarily used.

---

### 5. Auth Module
**Scope:** Authentication and user registration

**Screens:**
- LoginPage (full page)
- RegisterPage (full page)

**Routes:**
- `/auth/login` → LoginPage
- `/auth/register` → RegisterPage

**Components:**
- Login form
- Register form
- Social login buttons
- Login method tabs (username/email/phone)
- Password visibility toggles

**Shared Dependencies:**
- useAuth hook (shared)
- UI components (shared)

**Boundaries:**
- ✅ Can handle authentication
- ✅ Can redirect after login/register
- ❌ Cannot contain service logic
- ❌ Cannot contain order logic
- ❌ Cannot contain profile management (only initial registration)

**Notes:** Standalone authentication flow, no Header/Footer layout.

---

### 6. Profile Module
**Scope:** User profile management

**Screens:**
- ProfilePage (full page)

**Routes:**
- `/profile` → ProfilePage

**Components:**
- Profile header
- Profile information form
- Edit mode toggle
- Loyalty points card (link to loyalty page)

**Shared Dependencies:**
- Header (shared layout)
- Footer (shared layout)
- FloatingContact (shared global)
- ChatBox (shared global)
- useAuth hook (shared)

**Boundaries:**
- ✅ Can manage user profile information
- ✅ Can link to loyalty points page
- ❌ Cannot contain order management
- ❌ Cannot contain service booking
- ❌ Cannot contain authentication logic (only uses auth state)

**Notes:** Requires authentication. Links to Loyalty module but does not contain loyalty logic.

---

### 7. Orders Module
**Scope:** Order history and order management

**Screens:**
- OrderHistoryPage (full page)

**Routes:**
- `/orders` → OrderHistoryPage

**Components:**
- OrderCard
- OrderDetailModal
- InvoicePreviewModal
- ReviewModal
- ReviewList
- Status filter buttons
- Statistics cards

**Shared Dependencies:**
- Header (shared layout)
- Footer (shared layout)
- FloatingContact (shared global)
- ChatBox (shared global)
- useAuth hook (shared)
- ImageWithFallback (shared UI)

**Boundaries:**
- ✅ Can display order history
- ✅ Can show order details
- ✅ Can handle order reviews
- ✅ Can show invoices
- ❌ Cannot contain service booking
- ❌ Cannot contain profile management
- ❌ Cannot contain authentication logic (only uses auth state)

**Notes:** Requires authentication. Contains nested modals (OrderDetailModal → InvoicePreviewModal, ReviewModal).

---

### 8. Tasks Module
**Scope:** User dashboard with tabbed interface

**Screens:**
- TasksPage (full page)

**Routes:**
- `/tasks` → TasksPage

**Components:**
- Tab navigation (profile, orders, promotions)
- Profile tab content
- Orders tab content
- Promotions tab content
- OrderDetailModal (triggered from orders tab)
- Edit mode toggle (for profile tab)

**Shared Dependencies:**
- Header (shared layout)
- Footer (shared layout)
- FloatingContact (shared global)
- ChatBox (shared global)
- useAuth hook (shared)
- OrderCard (from Orders module - shared component)

**Boundaries:**
- ✅ Can display profile information (tab)
- ✅ Can display orders (tab)
- ✅ Can display promotions (tab)
- ❌ Cannot contain full profile management (only tab view)
- ❌ Cannot contain full order management (only tab view)
- ❌ Cannot contain authentication logic (only uses auth state)

**Notes:** 
- Requires authentication.
- This is a dashboard that aggregates content from Profile and Orders modules.
- Should reuse components from Profile and Orders modules where possible.
- Tabs are internal navigation, not separate routes.

---

### 9. Loyalty Module
**Scope:** Loyalty points and promotion program

**Screens:**
- LoyaltyPointsPage (full page)

**Routes:**
- `/loyalty-points` → LoyaltyPointsPage

**Components:**
- Points header
- Current tier badge
- Points history list
- Promotion tiers table
- Next tier progress

**Shared Dependencies:**
- Header (shared layout)
- Footer (shared layout)
- FloatingContact (shared global)
- ChatBox (shared global)
- useAuth hook (shared)

**Boundaries:**
- ✅ Can display loyalty points
- ✅ Can show promotion tiers
- ✅ Can show points history
- ❌ Cannot contain order management
- ❌ Cannot contain profile management
- ❌ Cannot contain authentication logic (only uses auth state)

**Notes:** Requires authentication. Linked from ProfilePage but is a separate module.

---

## Shared Components

### Layout Components
- **Header** - Navigation and user menu
- **Footer** - Company info and links
- **FloatingContact** - Floating contact buttons (global)

### Global Components
- **ChatBox** - Customer support chat (global)
- **ImageWithFallback** - Image component with fallback

### Shared Hooks
- **useAuth** - Authentication state and methods
- **useChat** - Chat state and methods

### Shared UI Components
- All components in `src/app/components/ui/` (button, input, dialog, etc.)

---

## Module Interaction Rules

### Allowed Cross-Module Links
- ✅ Home → Services, News, Contact
- ✅ Services → ServiceDetail
- ✅ Profile → Loyalty Points
- ✅ Any page → Login/Register (if not authenticated)
- ✅ Any page → Contact

### Forbidden Cross-Module Logic
- ❌ Services module cannot directly access Orders module logic
- ❌ Profile module cannot directly access Orders module logic
- ❌ Orders module cannot directly access Services module booking logic
- ❌ Auth module cannot access any other module's business logic

### Shared State Management
- ✅ useAuth hook - shared across all authenticated modules
- ✅ useChat hook - shared across all modules
- ❌ No shared state for orders, services, or profile (each module manages its own)

---

## Module Structure Template

Each module should follow this structure:

```
modules/
  {module-name}/
    {ModulePage}.tsx          # Entry point (if single page)
    components/                # Module-specific components
      Component1.tsx
      Component2.tsx
    hooks/                     # Module-specific hooks
      useModuleHook.ts
    api/                       # Module-specific API calls
      moduleApi.ts
    types/                     # Module-specific types
      module.types.ts
    index.ts                   # Public API
```

**Public API (index.ts) should only export:**
- Page components (entry points)
- Public hooks (if needed by other modules)
- Public types (if needed by other modules)

**Do NOT export:**
- Internal components
- Internal hooks
- Internal utilities

---

## Notes

1. **Tasks Module Special Case:** TasksPage is a dashboard that aggregates content from Profile and Orders modules. It should reuse components from those modules rather than duplicating logic.

2. **Modal Ownership:** Modals belong to the module that triggers them, not separate modules.

3. **Shared Components:** Layout and global components are in `shared/`, not in modules.

4. **Authentication:** Auth module handles authentication flow. Other modules only consume auth state via `useAuth` hook.

5. **No Deep Imports:** Modules should not import from other modules' internal components. Only use public APIs.
````

## File: next-env.d.ts
````typescript
/// <reference types="next" />
/// <reference types="next/image-types/global" />
/// <reference path="./.next/types/routes.d.ts" />

// NOTE: This file should not be edited
// see https://nextjs.org/docs/app/api-reference/config/typescript for more information.
````

## File: next.config.js
````javascript
/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  images: {
    domains: ['images.unsplash.com'],
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'images.unsplash.com',
      },
    ],
  },
}

module.exports = nextConfig
````

## File: ORDER_HISTORY_README.md
````markdown
# Trang Lịch Sử Đặt Dịch Vụ

## Tổng quan
Trang "Lịch sử đặt dịch vụ" là một tính năng quan trọng trong website sửa chữa điện nước, giúp khách hàng quản lý và theo dõi các đơn hàng của họ một cách dễ dàng và trực quan.

## Tính năng chính

### 1. Danh sách đơn hàng
- Hiển thị tất cả đơn hàng dưới dạng card với thông tin rõ ràng
- Mỗi card bao gồm:
  - Icon loại dịch vụ (điện, nước, mộc, vận chuyển, lắp đặt, đa dịch vụ)
  - Mã đơn hàng
  - Trạng thái đơn (với màu sắc phân biệt)
  - Ngày đặt dịch vụ
  - Địa chỉ sửa chữa
  - Tổng chi phí
  - Nút "Xem chi tiết"

### 2. Bộ lọc theo trạng thái
- **Tất cả**: Hiển thị tất cả đơn hàng
- **Chờ xử lý**: Đơn hàng mới đặt, chưa được xử lý
- **Đang xử lý**: Đơn hàng đang được xử lý (tìm thợ, xác nhận)
- **Đang sửa**: Thợ đang thực hiện sửa chữa
- **Hoàn thành**: Đơn hàng đã hoàn thành
- **Đã hủy**: Đơn hàng bị hủy

### 3. Thống kê nhanh
- Tổng số đơn hàng
- Số đơn hàng hoàn thành (màu xanh lá)
- Số đơn hàng đang xử lý (màu xanh dương)
- Số đơn hàng chờ xử lý (màu vàng)

### 4. Chi tiết đơn hàng (Modal)
Khi nhấn "Xem chi tiết", modal hiển thị:

#### Thông tin cơ bản
- Trạng thái đơn hàng
- Ngày đặt dịch vụ
- Thông tin khách hàng (họ tên, số điện thoại, địa chỉ)

#### Mô tả sự cố
- Mô tả chi tiết vấn đề
- Hình ảnh sự cố (nếu có)

#### Thông tin thợ sửa chữa
- Ảnh đại diện
- Tên thợ
- Số năm kinh nghiệm
- Đánh giá sao
- Nút gọi điện trực tiếp

#### Timeline tiến trình
- Đã đặt dịch vụ
- Thợ đang đến
- Đang sửa chữa
- Hoàn thành
- (Với visual indicators rõ ràng)

#### Chi tiết chi phí
- Danh sách các hạng mục
- Số lượng × Đơn giá
- Tổng cộng (nổi bật màu xanh dương)

#### Phương thức thanh toán
- Tiền mặt / Chuyển khoản / Chưa thanh toán

#### Ghi chú (nếu có)
- Các ghi chú đặc biệt về đơn hàng

### 5. Chức năng tương tác

#### Đặt lại dịch vụ
- Tạo yêu cầu đặt lại dịch vụ tương tự
- Hiển thị thông báo xác nhận

#### Gọi hỗ trợ
- Gọi trực tiếp đến số hotline hỗ trợ

#### Tải hóa đơn PDF
- Tải xuống hóa đơn (chỉ hiển thị với đơn hoàn thành)

#### Đánh giá dịch vụ
- Chỉ hiển thị với đơn hoàn thành chưa đánh giá
- Đánh giá bằng sao (1-5 sao)
- Nhập nhận xét (tối thiểu 10 ký tự)
- Gửi đánh giá

## Thiết kế UI/UX

### Màu sắc
- **Primary**: Xanh dương (#3B82F6)
- **Success**: Xanh lá (#10B981) - Hoàn thành
- **Warning**: Vàng (#F59E0B) - Chờ xử lý
- **Processing**: Xanh dương nhạt - Đang xử lý
- **Purple**: Tím (#8B5CF6) - Đang sửa
- **Gray**: Xám (#6B7280) - Đã hủy

### Responsive
- **Desktop**: Layout 2 cột cho danh sách đơn hàng
- **Mobile**: Layout 1 cột, filter buttons cuộn ngang
- **Modal**: Tự động điều chỉnh padding và kích thước

### Phân cấp thông tin
1. Tiêu đề trang (lớn, bold)
2. Bộ lọc (dễ nhìn, dễ tương tác)
3. Thống kê (cards nổi bật với màu sắc)
4. Danh sách đơn hàng (cards với hover effect)
5. Chi tiết (modal rõ ràng, dễ đọc)

## Truy cập trang

### Từ Header
1. Đăng nhập vào hệ thống
2. Click vào menu "Tôi" ở header
3. Chọn "Lịch sử đặt hàng"

### Direct URL
- Desktop/Mobile: `/orders`

## Dữ liệu mẫu
File `/src/app/data/ordersData.ts` chứa 7 đơn hàng mẫu với các trạng thái khác nhau:
1. Đơn sửa điện - Hoàn thành
2. Đơn sửa nước - Đang sửa
3. Đơn sửa mộc - Hoàn thành (đã đánh giá)
4. Đơn vận chuyển - Hoàn thành
5. Đơn lắp đặt - Đang xử lý
6. Đơn thông tắc - Đã hủy
7. Đơn đa dịch vụ - Hoàn thành

## Components

### OrderHistoryPage
- Main page component
- Quản lý state cho filter và selected order
- Location: `/src/app/pages/OrderHistoryPage.tsx`

### OrderCard
- Hiển thị thông tin tóm tắt của đơn hàng
- Props: order, onViewDetails
- Location: `/src/app/components/OrderCard.tsx`

### OrderDetailModal
- Hiển thị chi tiết đầy đủ của đơn hàng
- Props: order, onClose
- Location: `/src/app/components/OrderDetailModal.tsx`

## Hướng dẫn sử dụng cho khách hàng

### Xem danh sách đơn hàng
1. Đăng nhập vào tài khoản
2. Click vào "Tôi" → "Lịch sử đặt hàng"
3. Xem danh sách tất cả đơn hàng

### Lọc đơn hàng
1. Click vào các nút filter phía trên danh sách
2. Chọn trạng thái muốn xem (Tất cả, Chờ xử lý, Đang xử lý, Đang sửa, Hoàn thành, Đã hủy)
3. Danh sách sẽ tự động cập nhật

### Xem chi tiết đơn hàng
1. Click nút "Xem chi tiết" trên card đơn hàng
2. Modal chi tiết sẽ hiển thị với đầy đủ thông tin
3. Cuộn xuống để xem các phần khác nhau
4. Click nút X hoặc click bên ngoài modal để đóng

### Đặt lại dịch vụ
1. Mở chi tiết đơn hàng
2. Click nút "Đặt lại dịch vụ" ở phía dưới
3. Hệ thống sẽ tạo yêu cầu mới

### Gọi hỗ trợ
1. Mở chi tiết đơn hàng
2. Click nút "Gọi hỗ trợ"
3. Điện thoại sẽ tự động gọi đến số hotline

### Gọi điện cho thợ
1. Mở chi tiết đơn hàng có thông tin thợ
2. Click icon điện thoại màu xanh lá bên cạnh thông tin thợ
3. Điện thoại sẽ gọi trực tiếp đến thợ

### Đánh giá dịch vụ
1. Mở chi tiết đơn hàng đã hoàn thành
2. Click nút "Đánh giá dịch vụ"
3. Chọn số sao (1-5)
4. Nhập nhận xét (ít nhất 10 ký tự)
5. Click "Gửi đánh giá"

### Tải hóa đơn
1. Mở chi tiết đơn hàng đã hoàn thành
2. Click nút "Tải hóa đơn"
3. File PDF sẽ được tải xuống

## Tích hợp với hệ thống

### Routes
- Route: `/orders`
- Component: `OrderHistoryPage`
- Yêu cầu: Người dùng đã đăng nhập (nên implement)

### Context
- Sử dụng `UserContext` để kiểm tra trạng thái đăng nhập
- Có thể mở rộng để lấy dữ liệu đơn hàng theo user

### Toast Notifications
- Sử dụng `sonner` để hiển thị thông báo
- Success: Khi đặt lại dịch vụ, gửi đánh giá thành công
- Error: Khi có lỗi (ví dụ: nhập đánh giá quá ngắn)

## Khả năng mở rộng

### Tích hợp Backend
```typescript
// Ví dụ fetch orders từ API
const fetchOrders = async () => {
  const response = await fetch('/api/orders');
  const data = await response.json();
  return data;
};
```

### Real-time Updates
- Sử dụng WebSocket hoặc polling để cập nhật trạng thái real-time
- Thông báo khi có thay đổi trạng thái đơn hàng

### Phân trang
- Thêm pagination khi có nhiều đơn hàng
- Load more functionality

### Tìm kiếm
- Tìm kiếm theo mã đơn hàng
- Tìm kiếm theo địa chỉ
- Tìm kiếm theo loại dịch vụ

### Export
- Export danh sách đơn hàng ra Excel/CSV
- Print receipt

## Best Practices

### Performance
- Lazy load images trong modal
- Memoize components khi cần
- Virtual scrolling cho danh sách dài

### Accessibility
- Proper ARIA labels
- Keyboard navigation support
- Screen reader friendly

### Mobile Optimization
- Touch-friendly buttons (tối thiểu 44x44px)
- Swipe gestures để đóng modal
- Optimized images

## Ghi chú kỹ thuật

### TypeScript Types
- `Order`: Interface cho đơn hàng
- `OrderStatus`: Type cho trạng thái
- `TimelineStep`: Interface cho timeline
- `TechnicianInfo`: Interface cho thông tin thợ
- `CostDetail`: Interface cho chi tiết chi phí

### Helper Functions
- `getStatusText()`: Chuyển đổi status sang text tiếng Việt
- `getStatusColor()`: Lấy class Tailwind cho màu status
- `formatCurrency()`: Format số tiền theo VND
- `formatDate()`: Format ngày giờ theo locale Việt Nam

## Liên hệ & Hỗ trợ
- Email: suachuanho@gmail.com
- Hotline: 09xxxxxx
````

````json
{
  "name": "@figma/my-make-file",
  "private": true,
  "version": "0.0.1",
  "scripts": {
    "dev": "next dev",
    "build": "next build",
    "start": "next start",
    "lint": "next lint"
  },
  "dependencies": {
    "@emotion/react": "11.14.0",
    "@emotion/styled": "11.14.1",
    "@mui/icons-material": "7.3.5",
    "@mui/material": "7.3.5",
    "@popperjs/core": "2.11.8",
    "@radix-ui/react-accordion": "1.2.3",
    "@radix-ui/react-alert-dialog": "1.1.6",
    "@radix-ui/react-aspect-ratio": "1.1.2",
    "@radix-ui/react-avatar": "1.1.3",
    "@radix-ui/react-checkbox": "1.1.4",
    "@radix-ui/react-collapsible": "1.1.3",
    "@radix-ui/react-context-menu": "2.2.6",
    "@radix-ui/react-dialog": "1.1.6",
    "@radix-ui/react-dropdown-menu": "2.1.6",
    "@radix-ui/react-hover-card": "1.1.6",
    "@radix-ui/react-label": "2.1.2",
    "@radix-ui/react-menubar": "1.1.6",
    "@radix-ui/react-navigation-menu": "1.2.5",
    "@radix-ui/react-popover": "1.1.6",
    "@radix-ui/react-progress": "1.1.2",
    "@radix-ui/react-radio-group": "1.2.3",
    "@radix-ui/react-scroll-area": "1.2.3",
    "@radix-ui/react-select": "2.1.6",
    "@radix-ui/react-separator": "1.1.2",
    "@radix-ui/react-slider": "1.2.3",
    "@radix-ui/react-slot": "1.1.2",
    "@radix-ui/react-switch": "1.1.3",
    "@radix-ui/react-tabs": "1.1.3",
    "@radix-ui/react-toggle": "1.1.2",
    "@radix-ui/react-toggle-group": "1.1.2",
    "@radix-ui/react-tooltip": "1.1.8",
    "class-variance-authority": "0.7.1",
    "clsx": "2.1.1",
    "cmdk": "1.1.1",
    "date-fns": "3.6.0",
    "embla-carousel-react": "8.6.0",
    "html2canvas": "^1.4.1",
    "input-otp": "1.4.2",
    "jspdf": "^3.0.4",
    "lucide-react": "0.487.0",
    "motion": "12.23.24",
    "next-themes": "0.4.6",
    "phosphor-react": "^1.4.1",
    "react": "18.3.1",
    "react-day-picker": "8.10.1",
    "react-dnd": "16.0.1",
    "react-dnd-html5-backend": "16.0.1",
    "react-dom": "18.3.1",
    "react-hook-form": "7.55.0",
    "react-popper": "2.3.0",
    "react-resizable-panels": "2.1.7",
    "react-responsive-masonry": "2.7.1",
    "react-router-dom": "^7.10.1",
    "react-slick": "0.31.0",
    "recharts": "2.15.2",
    "sonner": "2.0.3",
    "tailwind-merge": "3.2.0",
    "tw-animate-css": "1.3.8",
    "vaul": "1.1.2"
  },
  "devDependencies": {
    "@types/node": "^20",
    "@types/react": "^18",
    "@types/react-dom": "^18",
    "autoprefixer": "^10.4.23",
    "eslint": "^8",
    "eslint-config-next": "^15",
    "next": "^15.1.0",
    "postcss": "^8",
    "tailwindcss": "^3.4.1",
    "typescript": "^5"
  },
  "peerDependencies": {
    "react": "18.3.1",
    "react-dom": "18.3.1"
  },
  "peerDependenciesMeta": {
    "react": {
      "optional": true
    },
    "react-dom": {
      "optional": true
    }
  },
  "pnpm": {
    "overrides": {
      "vite": "6.3.5"
    }
  }
}
````

## File: PHASE1_COMPLETE.md
````markdown
# Phase 1: Next.js Setup - COMPLETE ✅

## Summary

Phase 1 has been successfully completed. The Next.js App Router project structure has been created with all required directories and configuration files.

## What Was Done

### 1. Next.js Configuration
- ✅ `next.config.js` - Configured with React strict mode and image domains
- ✅ `tsconfig.json` - TypeScript configured with path aliases for modules, shared, and lib
- ✅ `postcss.config.mjs` - Updated for Next.js (Tailwind + Autoprefixer)
- ✅ `tailwind.config.ts` - Configured to scan app/, modules/, shared/, and src/ directories

### 2. Directory Structure Created
```
project-root/
├── app/                    # Next.js App Router (routing only)
│   ├── layout.tsx         # Root layout with metadata
│   ├── page.tsx           # Home page placeholder
│   ├── globals.css        # Global styles (imports from src/styles)
│   ├── services/
│   │   ├── page.tsx
│   │   └── [serviceId]/
│   │       └── page.tsx
│   ├── news/
│   │   └── page.tsx
│   ├── contact/
│   │   └── page.tsx
│   ├── tasks/
│   │   └── page.tsx
│   ├── login/
│   │   └── page.tsx
│   ├── register/
│   │   └── page.tsx
│   ├── profile/
│   │   └── page.tsx
│   ├── orders/
│   │   └── page.tsx
│   └── loyalty-points/
│       └── page.tsx
│
├── modules/                # Feature modules (empty, ready for Phase 3)
│   └── .gitkeep
│
├── shared/                 # Shared infrastructure (empty, ready for Phase 2)
│   ├── components/
│   ├── hooks/
│   ├── types/
│   └── utils/
│
└── lib/                    # Application-level config
    └── config.ts           # App configuration
```

### 3. Path Aliases Configured
- `@/*` → Root directory
- `@/modules/*` → `./modules/*`
- `@/shared/*` → `./shared/*`
- `@/lib/*` → `./lib/*`

### 4. Route Structure
All routes from the original React Router app have been created as placeholder pages in the Next.js App Router structure:
- `/` - Home (app/page.tsx)
- `/services` - Services listing
- `/services/[serviceId]` - Service detail (dynamic route)
- `/news` - News listing
- `/contact` - Contact page
- `/tasks` - Tasks/Dashboard page
- `/login` - Login page
- `/register` - Register page
- `/profile` - Profile page
- `/orders` - Order history
- `/loyalty-points` - Loyalty points

## What Was NOT Done (As Per Requirements)

- ❌ No feature modules migrated
- ❌ No components moved
- ❌ No UI/JSX changes
- ❌ No styling changes
- ❌ No business logic added to app/ routes
- ❌ No contexts or providers migrated

## Known Issues (To be resolved in Phase 2)

### CSS Import Issue
The existing `src/styles/theme.css` file uses `@layer base` directives which require `@tailwind base` to be in the same processing context. Since CSS `@import` processes files separately in Next.js, this causes a build error.

**Current Status:** Build will fail until CSS is properly migrated in Phase 2.

**Resolution Plan:** In Phase 2, we will:
- Properly migrate CSS files to work with Next.js
- Ensure `@tailwind` directives and `@layer` directives are in the correct processing context
- Preserve all existing styles and theme variables

This is expected and will be resolved during the shared infrastructure migration phase.

## Next Steps

**Phase 2: Shared Infrastructure Migration**
- Migrate and fix CSS structure for Next.js compatibility
- Migrate contexts (UserContext, ChatContext) to shared or module hooks
- Migrate layout components (Header, Footer, FloatingContact) to shared/components/layout/
- Migrate shared UI components
- Set up providers in app/layout.tsx

**Ready for Phase 2 approval.**
````

## File: PHASE2_COMPLETE.md
````markdown
# Phase 2: Shared Infrastructure Migration - COMPLETE ✅

## Summary

Phase 2 has been successfully completed. The CSS build issue has been resolved, and all shared infrastructure (contexts, providers, utilities) has been migrated to the `shared/` directory.

## What Was Done

### 1. CSS Build Issue Resolution ✅

**Problem:** The `@layer base` directives in `src/styles/theme.css` required `@tailwind base` to be in the same processing context, but CSS `@import` processes files separately in Next.js.

**Solution:**
- Moved `@layer base` content from `src/styles/theme.css` to `app/globals.css` after `@tailwind base` directives
- Replaced `@apply` directives with direct CSS properties using CSS variables to avoid processing order issues:
  - `@apply border-border` → `border-color: var(--border)`
  - `@apply bg-background text-foreground` → `background-color: var(--background); color: var(--foreground)`
- Preserved all CSS variables and theme definitions in `src/styles/theme.css`
- All visual styling remains **100% identical** - only the processing structure changed

**Files Modified:**
- `app/globals.css` - Added `@layer base` content after `@tailwind` directives
- `src/styles/theme.css` - Removed `@layer base` block (CSS variables preserved)
- `tailwind.config.ts` - Added custom color mappings for compatibility

### 2. Shared Infrastructure Migration ✅

#### Contexts → Shared Hooks

**Migrated:**
- `src/app/contexts/UserContext.tsx` → `shared/hooks/useAuth.tsx`
  - Renamed to `AuthProvider` and `useAuth` (with backward compatibility exports)
  - Added `"use client"` directive for Next.js
  - Exported as both `useAuth` and `useUser` for compatibility

- `src/app/contexts/ChatContext.tsx` → `shared/hooks/useChat.tsx`
  - Preserved exact functionality
  - Added `"use client"` directive for Next.js

#### Shared Utilities

**Migrated:**
- `src/app/components/ui/utils.ts` → `shared/utils/cn.ts`
  - `cn()` function for Tailwind class merging
  - Preserved exact implementation

- `src/app/components/ui/use-mobile.ts` → `shared/hooks/useIsMobile.ts`
  - Mobile detection hook
  - Preserved exact implementation with matchMedia API

#### Providers Setup

**Updated:**
- `app/layout.tsx` - Added `AuthProvider` and `ChatProvider` wrappers
  - Providers are now in the root layout
  - All pages have access to auth and chat context

#### Public API Files

**Created:**
- `shared/hooks/index.ts` - Exports all shared hooks
- `shared/utils/index.ts` - Exports all shared utilities
- `shared/types/index.ts` - Placeholder for shared types

### 3. Build Verification ✅

- ✅ Build compiles successfully
- ✅ All 13 routes generate correctly
- ✅ TypeScript type checking passes
- ✅ No linting errors
- ✅ CSS processes correctly

### 4. Directory Structure After Phase 2

```
shared/
├── hooks/
│   ├── useAuth.tsx          # Auth context (UserContext migrated)
│   ├── useChat.tsx          # Chat context
│   ├── useIsMobile.ts       # Mobile detection hook
│   └── index.ts             # Public API
├── utils/
│   ├── cn.ts                # Class name utility
│   └── index.ts             # Public API
└── types/
    └── index.ts             # Shared types (placeholder)

app/
└── layout.tsx               # Root layout with providers
```

## What Was NOT Done (As Per Requirements)

- ❌ No feature modules extracted
- ❌ No UI components migrated
- ❌ No JSX structure changes
- ❌ No component refactoring
- ❌ No new libraries introduced
- ❌ No visual/UI changes

## CSS Changes Summary

**Technical Changes (No Visual Impact):**
1. Moved `@layer base` block to `app/globals.css` (required for Next.js)
2. Replaced `@apply` directives with direct CSS properties (same visual result)
3. All CSS variables, colors, animations, and styles preserved exactly

**Visual Preservation:**
- ✅ All Tailwind classes remain identical
- ✅ All CSS variables preserved
- ✅ All animations preserved
- ✅ All theme colors preserved
- ✅ Zero visual changes

## Files Created/Modified

### Created:
- `shared/hooks/useAuth.tsx`
- `shared/hooks/useChat.tsx`
- `shared/hooks/useIsMobile.ts`
- `shared/hooks/index.ts`
- `shared/utils/cn.ts`
- `shared/utils/index.ts`
- `shared/types/index.ts`

### Modified:
- `app/globals.css` - CSS structure fix
- `app/layout.tsx` - Added providers
- `src/styles/theme.css` - Removed @layer block (variables preserved)
- `tailwind.config.ts` - Added custom color mappings
- `tsconfig.json` - Excluded old Vite entry point
- `app/services/[serviceId]/page.tsx` - Fixed Next.js 15 async params

## Next Steps

**Phase 3: Module Extraction**
- Extract feature modules one at a time
- Start with Home module
- Move feature-specific components, hooks, and data
- Maintain strict module boundaries

**Ready for Phase 3 approval.**
````

## File: PHASE3_COMPLETE.md
````markdown
# Phase 3: Home Module Extraction - IN PROGRESS ⚠️

## Summary

Phase 3 has extracted the Home module structure, but there's a TypeScript build issue with the AuthProvider/ChatProvider that needs to be resolved.

## What Was Done

### 1. Home Module Structure Created ✅

**Directory Structure:**
```
modules/home/
├── components/
│   ├── HeroSection.tsx
│   ├── AboutSection.tsx
│   ├── ServiceCategories.tsx
│   ├── ServiceWorkerSelector.tsx
│   ├── WorkerSelectionModal.tsx
│   ├── TestimonialsSection.tsx
│   └── HomePage.tsx
├── hooks/
├── api/
├── types/
└── index.ts
```

### 2. Components Migrated ✅

**Moved Components:**
- `HeroSection` → `modules/home/components/HeroSection.tsx`
- `AboutSection` → `modules/home/components/AboutSection.tsx`
- `ServiceCategories` → `modules/home/components/ServiceCategories.tsx`
- `ServiceWorkerSelector` → `modules/home/components/ServiceWorkerSelector.tsx`
- `WorkerSelectionModal` → `modules/home/components/WorkerSelectionModal.tsx`
- `TestimonialsSection` → `modules/home/components/TestimonialsSection.tsx`
- `HomePage` → `modules/home/components/HomePage.tsx`

### 3. Navigation Updated ✅

**Changes:**
- Replaced `useNavigate()` from `react-router-dom` with `useRouter()` from `next/navigation`
- Replaced `navigate('/path')` with `router.push('/path')`
- All components marked with `"use client"` directive for Next.js

### 4. Module Public API Created ✅

**Created:**
- `modules/home/index.ts` - Exports `HomePage` component

### 5. App Page Updated ✅

**Updated:**
- `app/page.tsx` - Now imports and renders `HomePage` from `@/modules/home`

## Current Issue

**TypeScript Build Error:**
- `AuthProvider` and `ChatProvider` have type inference issues
- TypeScript infers return type as `{}` instead of proper JSX element
- This is blocking the build completion

**Files Affected:**
- `shared/hooks/useAuth.tsx`
- `shared/hooks/useChat.tsx`
- `app/providers.tsx`

## Files Created

### Home Module:
- `modules/home/components/HeroSection.tsx`
- `modules/home/components/AboutSection.tsx`
- `modules/home/components/ServiceCategories.tsx`
- `modules/home/components/ServiceWorkerSelector.tsx`
- `modules/home/components/WorkerSelectionModal.tsx`
- `modules/home/components/TestimonialsSection.tsx`
- `modules/home/components/HomePage.tsx`
- `modules/home/index.ts`

### Infrastructure:
- `app/providers.tsx` - Client component wrapper for providers

## Files Modified

- `app/page.tsx` - Updated to use Home module
- `shared/hooks/useAuth.tsx` - Converted to React.FC pattern (type issue persists)
- `shared/hooks/useChat.tsx` - Converted to React.FC pattern (type issue persists)
- `app/layout.tsx` - Updated to use Providers component

## Next Steps

1. Resolve TypeScript type inference issue with AuthProvider/ChatProvider
2. Verify build completes successfully
3. Verify UI remains unchanged
4. Complete Phase 3 summary

## UI Preservation

- ✅ All JSX structure preserved
- ✅ All Tailwind classes preserved
- ✅ All component logic preserved
- ✅ Only navigation method changed (react-router → Next.js router)
````

## File: postcss.config.mjs
````
/**
 * PostCSS Configuration for Next.js
 *
 * Next.js requires explicit PostCSS configuration for Tailwind CSS
 */
export default {
  plugins: {
    tailwindcss: {},
    autoprefixer: {},
  },
};
````

## File: ROUTE_DECISION_TABLE.md
````markdown
# Route Decision Table

## Analysis Date
Based on original project scope: `repomix-output-Home Page Design.zip.md`

**Decision Criteria:**
- Design intent (user navigation flow)
- URL shareability
- Browser history support
- SEO considerations
- Authentication requirements

---

## Route Decisions

| Screen/Feature | Type | Route | Rationale |
|----------------|------|-------|------------|
| **HomePage** | Full Page | `/` | Primary landing page, shareable URL |
| **ServicesPage** | Full Page | `/services` | Service listing, shareable, SEO-friendly |
| **ServiceDetailPage** | Full Page | `/services/:serviceId` | Individual service detail, shareable, SEO-friendly, dynamic route |
| **NewsPage** | Full Page | `/news` | News listing, shareable, SEO-friendly |
| **ContactPage** | Full Page | `/contact` | Contact information, shareable |
| **TasksPage** | Full Page | `/tasks` | Dashboard, requires auth, tabbed interface |
| **LoginPage** | Full Page | `/auth/login` | Authentication flow, separate from `/login` for clarity |
| **RegisterPage** | Full Page | `/auth/register` | Authentication flow, separate from `/register` for clarity |
| **ProfilePage** | Full Page | `/profile` | User profile, requires auth, shareable (if public profiles) |
| **OrderHistoryPage** | Full Page | `/orders` | Order management, requires auth |
| **LoyaltyPointsPage** | Full Page | `/loyalty-points` | Loyalty program, requires auth |

---

## UI States - Routing Decision

| UI State | Type | Route Decision | Rationale |
|----------|------|----------------|-----------|
| **BookServiceModal** | Modal | No route | Modal overlay, not shareable, temporary state |
| **ServiceDetailModal** | Modal | No route | Quick preview, not shareable |
| **OrderDetailModal** | Modal | No route | Detail view, not shareable, nested in orders page |
| **InvoicePreviewModal** | Modal | No route | PDF preview, nested in order detail |
| **ReviewModal** | Modal | No route | Form submission, temporary state |
| **WorkerSelectionModal** | Modal | No route | Selection interface, temporary state |
| **ChatBox** | Floating | No route | Always available, global component |
| **FloatingContact** | Floating | No route | Always available, global component |
| **TasksPage Tabs** | Tab State | No route (or query param) | Internal navigation, could use query param `/tasks?tab=orders` for shareability |
| **LoginPage Tabs** | Tab State | No route | Form input method, temporary state |
| **Profile Edit Mode** | Toggle | No route | Edit state, temporary |
| **Order Filter** | Filter State | Query param (optional) | Could use `/orders?status=completed` for shareability |

---

## Route Grouping by Module

### Home Module
- `/` → HomePage

### Services Module
- `/services` → ServicesPage
- `/services/:serviceId` → ServiceDetailPage

### News Module
- `/news` → NewsPage

### Contact Module
- `/contact` → ContactPage

### Auth Module
- `/auth/login` → LoginPage
- `/auth/register` → RegisterPage

### Profile Module
- `/profile` → ProfilePage

### Orders Module
- `/orders` → OrderHistoryPage

### Tasks Module
- `/tasks` → TasksPage (with internal tabs)

### Loyalty Module
- `/loyalty-points` → LoyaltyPointsPage

---

## Authentication Requirements

| Route | Auth Required | Redirect If Not Authenticated |
|-------|---------------|-------------------------------|
| `/` | No | - |
| `/services` | No | - |
| `/services/:serviceId` | No | - |
| `/news` | No | - |
| `/contact` | No | - |
| `/auth/login` | No | - |
| `/auth/register` | No | - |
| `/tasks` | Yes | Redirect to `/auth/login` |
| `/profile` | Yes | Redirect to `/` or `/auth/login` |
| `/orders` | Yes | Redirect to `/auth/login` |
| `/loyalty-points` | Yes | Redirect to `/auth/login` |

---

## Route Aliases (Optional)

For backward compatibility or user convenience:

- `/login` → `/auth/login` (redirect)
- `/register` → `/auth/register` (redirect)

---

## Query Parameters (Optional Enhancements)

### TasksPage
- `/tasks?tab=profile` - Direct link to profile tab
- `/tasks?tab=orders` - Direct link to orders tab
- `/tasks?tab=promotions` - Direct link to promotions tab

### OrderHistoryPage
- `/orders?status=completed` - Filter by status
- `/orders?status=pending` - Filter by status

### ServiceDetailPage
- `/services/:serviceId?section=pricing` - Scroll to pricing section
- `/services/:serviceId?section=reviews` - Scroll to reviews section

---

## Route Hierarchy

```
/ (Home)
├── /services (Services Listing)
│   └── /services/:serviceId (Service Detail)
├── /news (News Listing)
├── /contact (Contact)
├── /auth (Authentication)
│   ├── /auth/login (Login)
│   └── /auth/register (Register)
└── /[authenticated] (Requires Auth)
    ├── /tasks (Dashboard)
    ├── /profile (Profile)
    ├── /orders (Order History)
    └── /loyalty-points (Loyalty Points)
```

---

## Notes

1. **Modals are NOT routes:** All modals are UI states, not separate routes. They are triggered by user actions and do not appear in browser history.

2. **Tab states:** TasksPage tabs could optionally use query parameters for shareability, but are not required routes.

3. **Authentication flow:** Login and Register are grouped under `/auth/*` for better organization and clarity.

4. **Service detail:** Dynamic route `/services/:serviceId` allows for shareable service URLs and SEO.

5. **No nested routes:** All pages are top-level routes. No nested routing structure needed.

6. **Floating components:** ChatBox and FloatingContact are global components, not routes.
````

## File: tailwind.config.ts
````typescript
import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
    "./modules/**/*.{js,ts,jsx,tsx,mdx}",
    "./shared/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/**/*.{js,ts,jsx,tsx,mdx}", // Include src for existing components during migration
  ],
  theme: {
    extend: {
      colors: {
        border: "var(--border)",
        background: "var(--background)",
        foreground: "var(--foreground)",
        ring: "var(--ring)",
        // Add other custom colors as needed
      },
    },
  },
  plugins: [],
};

export default config;
````

## File: tsconfig.json
````json
{
  "compilerOptions": {
    "target": "ES2017",
    "lib": ["dom", "dom.iterable", "esnext"],
    "allowJs": true,
    "skipLibCheck": true,
    "strict": true,
    "noEmit": true,
    "esModuleInterop": true,
    "module": "esnext",
    "moduleResolution": "bundler",
    "resolveJsonModule": true,
    "isolatedModules": true,
    "jsx": "preserve",
    "incremental": true,
    "plugins": [
      {
        "name": "next"
      }
    ],
    "paths": {
      "@/*": ["./*"],
      "@/modules/*": ["./modules/*"],
      "@/shared/*": ["./shared/*"],
      "@/lib/*": ["./lib/*"]
    }
  },
  "include": [
    "next-env.d.ts",
    "**/*.ts",
    "**/*.tsx",
    ".next/types/**/*.ts"
  ],
  "exclude": [
    "node_modules",
    "src/main.tsx",
    "vite.config.ts"
  ]
}
````

## File: vite.config.ts
````typescript
import { defineConfig } from 'vite'
import path from 'path'
import tailwindcss from '@tailwindcss/vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [
    // The React and Tailwind plugins are both required for Make, even if
    // Tailwind is not being actively used – do not remove them
    react(),
    tailwindcss(),
  ],
  resolve: {
    alias: {
      // Alias @ to the src directory
      '@': path.resolve(__dirname, './src'),
    },
  },
})
````
