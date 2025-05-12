document.addEventListener('DOMContentLoaded', () => {
    // Check if API_CONFIG exists and is loaded properly
    if (!window.API_CONFIG) {
        console.error("API_CONFIG not found. Make sure config.js is loaded before script.js");
        // Create a fallback config
        window.API_CONFIG = {
            baseUrl: 'http://localhost:8080',
            getUrl: function(endpoint) {
                return this.baseUrl + '/api/' + endpoint;
            },
            fetchOptions: {
                mode: 'cors',
                credentials: 'omit',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            }
        };
    }

    // API Endpoints
    const API_BASE = {
        shoes: API_CONFIG.getUrl('shoes'),
        brands: API_CONFIG.getUrl('brands'),
        categories: API_CONFIG.getUrl('categories'),
        cart: API_CONFIG.getUrl('cart'),
        orders: API_CONFIG.getUrl('orders'),
        users: API_CONFIG.getUrl('users')
    };

    // App State
    let currentUser = null;
    let shoes = [];
    let brands = [];
    let categories = [];
    let cart = [];
    let selectedShoe = null;
    let selectedSize = null;
    let selectedColor = null;

    // DOM Elements
    const productList = document.getElementById('product-list');
    const cartItems = document.getElementById('cart-items');
    const cartCount = document.getElementById('cart-count');
    const cartTotal = document.getElementById('cart-total');
    const loginForm = document.getElementById('login-form');
    const checkoutForm = document.getElementById('checkout-form');
    
    // Modals
    const loginModal = document.getElementById('login-modal');
    const shoeDetailModal = document.getElementById('shoe-detail-modal');
    
    // Buttons
    const loginBtn = document.getElementById('login-btn');
    const closeLoginBtn = document.getElementById('close-login');
    const cartBtn = document.getElementById('cart-btn');
    const closeCartBtn = document.getElementById('close-cart');
    const checkoutBtn = document.getElementById('checkout-btn');
    const closeDetailBtn = document.getElementById('close-detail');
    const addToCartBtn = document.getElementById('add-to-cart-btn');
    const applyFilterBtn = document.getElementById('apply-filter');
    const resetFilterBtn = document.getElementById('reset-filter');

    // Filters
    const filterBrand = document.getElementById('filter-brand');
    const filterCategory = document.getElementById('filter-category');
    const filterGender = document.getElementById('filter-gender');
    const filterPriceMin = document.getElementById('filter-price-min');
    const filterPriceMax = document.getElementById('filter-price-max');

    // Sections
    const productSection = document.getElementById('product-list');
    const cartSection = document.getElementById('cart-section');
    const checkoutSection = document.getElementById('checkout-section');

    // Detail Elements
    const detailImage = document.getElementById('detail-image');
    const detailName = document.getElementById('detail-name');
    const detailBrand = document.getElementById('detail-brand');
    const detailCategory = document.getElementById('detail-category');
    const detailPrice = document.getElementById('detail-price');
    const detailDescription = document.getElementById('detail-description');
    const detailSizes = document.getElementById('detail-sizes');
    const detailColors = document.getElementById('detail-colors');
    const detailQuantity = document.getElementById('detail-quantity');

    // Initialize App
    init();

    // App Initialization
    async function init() {
        setupEventListeners();
        await Promise.all([
            loadShoes(),
            loadBrands(),
            loadCategories()
        ]);
        
        // Set min date for delivery (2 days from now)
        const deliveryDateInput = document.getElementById('delivery-date');
        if (deliveryDateInput) {
            const today = new Date();
            const minDate = new Date(today.setDate(today.getDate() + 2));
            deliveryDateInput.min = minDate.toISOString().split('T')[0];
        }
    }

    // Setup Event Listeners
    function setupEventListeners() {
        // Login
        loginBtn.addEventListener('click', () => {
            loginModal.style.display = 'flex';
        });
        
        closeLoginBtn.addEventListener('click', () => {
            loginModal.style.display = 'none';
        });
        
        loginForm.addEventListener('submit', handleLogin);
        
        // Cart
        cartBtn.addEventListener('click', () => {
            if (!currentUser) {
                alert('Vui lòng đăng nhập để xem giỏ hàng');
                loginModal.style.display = 'flex';
                return;
            }
            showCart();
        });
        
        closeCartBtn.addEventListener('click', () => {
            hideCart();
        });
        
        checkoutBtn.addEventListener('click', () => {
            if (cart.length === 0) {
                alert('Giỏ hàng của bạn đang trống!');
                return;
            }
            cartSection.classList.add('hidden');
            checkoutSection.classList.remove('hidden');
        });
        
        // Checkout
        checkoutForm.addEventListener('submit', handleCheckout);
        
        // Shoe Detail
        closeDetailBtn.addEventListener('click', () => {
            shoeDetailModal.style.display = 'none';
        });
        
        addToCartBtn.addEventListener('click', () => {
            if (!currentUser) {
                alert('Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng');
                shoeDetailModal.style.display = 'none';
                loginModal.style.display = 'flex';
                return;
            }
            
            if (!selectedSize || !selectedColor) {
                alert('Vui lòng chọn size và màu sắc');
                return;
            }
            
            addToCart(
                selectedShoe.id,
                selectedSize,
                selectedColor,
                parseInt(detailQuantity.value)
            );
        });
        
        // Filters
        applyFilterBtn.addEventListener('click', applyFilters);
        resetFilterBtn.addEventListener('click', resetFilters);
    }

    // Load Data Functions
    async function loadShoes() {
        try {
            const response = await fetch(API_BASE.shoes, {
                ...API_CONFIG.fetchOptions,
                method: 'GET'
            });
            shoes = await response.json();
            displayShoes(shoes);
        } catch (error) {
            console.error('Lỗi khi tải danh sách giày:', error);
        }
    }
    
    async function loadBrands() {
        try {
            const response = await fetch(API_BASE.brands, {
                ...API_CONFIG.fetchOptions,
                method: 'GET'
            });
            brands = await response.json();
            populateBrandFilter(brands);
        } catch (error) {
            console.error('Lỗi khi tải thương hiệu:', error);
        }
    }
    
    async function loadCategories() {
        try {
            const response = await fetch(API_BASE.categories, {
                ...API_CONFIG.fetchOptions,
                method: 'GET'
            });
            categories = await response.json();
            populateCategoryFilter(categories);
        } catch (error) {
            console.error('Lỗi khi tải danh mục:', error);
        }
    }
    
    async function loadShoeDetail(shoeId) {
        try {
            const response = await fetch(`${API_BASE.shoes}/${shoeId}`, {
                ...API_CONFIG.fetchOptions,
                method: 'GET'
            });
            const shoe = await response.json();
            displayShoeDetail(shoe);
        } catch (error) {
            console.error('Lỗi khi tải chi tiết giày:', error);
        }
    }
    
    async function loadShoeInventory(shoeId) {
        try {
            const response = await fetch(`${API_BASE.shoes}/${shoeId}/inventory`, {
    ...API_CONFIG.fetchOptions,
    method: 'GET'
            });
            const inventory = await response.json();
            return inventory;
        } catch (error) {
            console.error('Lỗi khi tải kho giày:', error);
            return [];
        }
    }
    
    async function loadCart() {
        if (!currentUser) return;
        
        try {
            const authOptions = {
                ...API_CONFIG.fetchOptions,
                method: 'GET',
                headers: { 
                    ...API_CONFIG.fetchOptions.headers,
                    'Authorization': 'Basic ' + btoa(`${currentUser.username}:${currentUser.password || ''}`) 
                }
            };
            
            const response = await fetch(`${API_BASE.cart}/${currentUser.username}/items`, authOptions);
            cart = await response.json();
            updateCartCount();
        } catch (error) {
            console.error('Lỗi khi tải giỏ hàng:', error);
        }
    }

    // Display Functions
    function displayShoes(shoes) {
        productList.innerHTML = '';
            
        if (shoes.length === 0) {
            productList.innerHTML = '<p class="col-span-3 text-center py-8">Không tìm thấy giày phù hợp</p>';
            return;
        }
        
        shoes.forEach(shoe => {
            const card = document.createElement('div');
            card.className = 'product-card bg-white p-4 rounded shadow';
            card.innerHTML = `
                <img src="${shoe.imageUrl || 'https://via.placeholder.com/300x200?text=No+Image'}" 
                     alt="${shoe.name}" 
                     class="w-full h-48 object-cover rounded mb-2">
                <h3 class="text-lg font-bold">${shoe.name}</h3>
                <p class="text-blue-600">${shoe.brand ? shoe.brand.name : ''}</p>
                <p class="text-gray-600">${shoe.category ? shoe.category.name : ''}</p>
                <p class="text-green-600 font-bold">${shoe.price.toLocaleString('vi-VN')} VND</p>
                <button class="view-detail-btn bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 mt-2 w-full"
                        data-id="${shoe.id}">Xem chi tiết</button>
            `;
            productList.appendChild(card);
            
            // Add event listener to view detail button
            card.querySelector('.view-detail-btn').addEventListener('click', () => {
                loadShoeDetail(shoe.id);
            });
        });
    }
    
    async function displayShoeDetail(shoe) {
        selectedShoe = shoe;
        selectedSize = null;
        selectedColor = null;
        
        // Set basic info
        detailImage.src = shoe.imageUrl || 'https://via.placeholder.com/300x400?text=No+Image';
        detailName.textContent = shoe.name;
        detailBrand.textContent = shoe.brand ? shoe.brand.name : '';
        detailCategory.textContent = shoe.category ? shoe.category.name : '';
        detailPrice.textContent = `${shoe.price.toLocaleString('vi-VN')} VND`;
        detailDescription.textContent = shoe.description || 'Không có mô tả';
        
        // Load inventory data
        const inventory = await loadShoeInventory(shoe.id);
        
        // Get unique sizes and colors
        const sizes = [...new Set(inventory.map(item => item.size))];
        const colors = [...new Set(inventory.map(item => item.color))];
        
        // Display sizes
        detailSizes.innerHTML = '';
        sizes.forEach(size => {
            const sizeBtn = document.createElement('button');
            sizeBtn.className = 'border p-2 rounded hover:bg-gray-100';
            sizeBtn.textContent = size;
            sizeBtn.addEventListener('click', () => {
                // Remove active class from all size buttons
                document.querySelectorAll('#detail-sizes button').forEach(btn => {
                    btn.classList.remove('bg-blue-500', 'text-white');
                    btn.classList.add('hover:bg-gray-100');
                });
                
                // Add active class to selected button
                sizeBtn.classList.remove('hover:bg-gray-100');
                sizeBtn.classList.add('bg-blue-500', 'text-white');
                
                // Set selected size
                selectedSize = size;
            });
            detailSizes.appendChild(sizeBtn);
        });
        
        // Display colors
        detailColors.innerHTML = '';
        colors.forEach(color => {
            const colorBtn = document.createElement('button');
            colorBtn.className = 'border p-2 rounded hover:bg-gray-100';
            colorBtn.textContent = color;
            colorBtn.addEventListener('click', () => {
                // Remove active class from all color buttons
                document.querySelectorAll('#detail-colors button').forEach(btn => {
                    btn.classList.remove('bg-blue-500', 'text-white');
                    btn.classList.add('hover:bg-gray-100');
                });
                
                // Add active class to selected button
                colorBtn.classList.remove('hover:bg-gray-100');
                colorBtn.classList.add('bg-blue-500', 'text-white');
                
                // Set selected color
                selectedColor = color;
            });
            detailColors.appendChild(colorBtn);
        });
        
        // Reset quantity
        detailQuantity.value = 1;
        
        // Show modal
        shoeDetailModal.style.display = 'flex';
    }
    
    function displayCart() {
        cartItems.innerHTML = '';
        
        if (cart.length === 0) {
            cartItems.innerHTML = '<p class="text-center py-4">Giỏ hàng của bạn đang trống</p>';
            cartTotal.textContent = '0';
            return;
        }
        
        let total = 0;
        
        cart.forEach(item => {
            const cartItem = document.createElement('div');
            cartItem.className = 'bg-white p-4 rounded shadow flex justify-between items-center';
            
            // Get shoe info
            const shoe = shoes.find(s => s.id === item.shoeId);
            
            if (!shoe) {
                console.warn('Không tìm thấy thông tin giày cho item:', item);
        return;
    }

            total += shoe.price * item.quantity;
            
            cartItem.innerHTML = `
                <div class="flex-1">
                    <p class="font-bold">${shoe.name}</p>
                    <p>Size: ${item.size} | Màu: ${item.color}</p>
                    <p>Đơn giá: ${shoe.price.toLocaleString('vi-VN')} VND</p>
                    <div class="flex items-center mt-2">
                        <span>Số lượng:</span>
                        <button class="cart-minus-btn px-2 py-1 bg-gray-200 rounded ml-2" data-id="${item.id}">-</button>
                        <span class="mx-2">${item.quantity}</span>
                        <button class="cart-plus-btn px-2 py-1 bg-gray-200 rounded" data-id="${item.id}">+</button>
                    </div>
                </div>
                <div class="flex flex-col items-end">
                    <p class="font-bold">${(shoe.price * item.quantity).toLocaleString('vi-VN')} VND</p>
                    <button class="cart-remove-btn bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 mt-2" data-id="${item.id}">Xóa</button>
                </div>
            `;
            
            cartItems.appendChild(cartItem);
            
            // Add event listeners for cart item buttons
            cartItem.querySelector('.cart-minus-btn').addEventListener('click', () => {
                updateCartItemQuantity(item.id, item.quantity - 1);
            });
            
            cartItem.querySelector('.cart-plus-btn').addEventListener('click', () => {
                updateCartItemQuantity(item.id, item.quantity + 1);
            });
            
            cartItem.querySelector('.cart-remove-btn').addEventListener('click', () => {
                removeFromCart(item.id);
            });
        });
        
        cartTotal.textContent = total.toLocaleString('vi-VN');
    }
    
    function populateBrandFilter(brands) {
        filterBrand.innerHTML = '<option value="">Tất cả</option>';
        brands.forEach(brand => {
            const option = document.createElement('option');
            option.value = brand.id;
            option.textContent = brand.name;
            filterBrand.appendChild(option);
        });
    }
    
    function populateCategoryFilter(categories) {
        filterCategory.innerHTML = '<option value="">Tất cả</option>';
        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category.id;
            option.textContent = category.name;
            filterCategory.appendChild(option);
        });
    }

    // Action Functions
    async function handleLogin(e) {
        e.preventDefault();
        
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        
        try {
            // For this specific endpoint, we need to send authorization
            const authOptions = {
                ...API_CONFIG.fetchOptions,
                method: 'GET',
                headers: { 
                    ...API_CONFIG.fetchOptions.headers,
                    'Authorization': 'Basic ' + btoa(`${username}:${password}`) 
                }
            };
            
            const response = await fetch(`${API_BASE.users}/${username}/permission`, authOptions);
            
            if (!response.ok) {
                throw new Error('Đăng nhập không thành công. Vui lòng kiểm tra lại thông tin.');
            }
            
            const hasPermission = await response.json();
            
            if (!hasPermission) {
                throw new Error('Tài khoản của bạn không có quyền truy cập.');
            }
            
            // Get user info
            const userResponse = await fetch(`${API_BASE.users}/${username}`, authOptions);
            
            if (!userResponse.ok) {
                throw new Error('Không thể lấy thông tin người dùng.');
            }
            
            currentUser = await userResponse.json();
            
            // If we only get permission but not user details
            if (!currentUser || typeof currentUser !== 'object') {
                currentUser = { username: username };
            }
            
            // Save password for future authenticated requests
            currentUser.password = password;
            
            // Update UI
            loginBtn.textContent = `Xin chào, ${currentUser.name || currentUser.username}`;
            loginModal.style.display = 'none';
            
            // Load cart
            await loadCart();
            
            alert('Đăng nhập thành công!');
        } catch (error) {
            console.error('Lỗi đăng nhập:', error);
            alert(error.message);
        }
    }
    
    async function addToCart(shoeId, size, color, quantity) {
        if (!currentUser) {
            alert('Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng');
            return;
        }
        
        try {
            const authOptions = {
                ...API_CONFIG.fetchOptions,
                method: 'POST',
                headers: { 
                    ...API_CONFIG.fetchOptions.headers,
                    'Authorization': 'Basic ' + btoa(`${currentUser.username}:${currentUser.password || ''}`) 
                },
                body: JSON.stringify({ shoeId, size, color, quantity })
            };
            
            const response = await fetch(`${API_BASE.cart}/${currentUser.username}/items`, authOptions);
            
            if (!response.ok) {
                throw new Error('Không thể thêm sản phẩm vào giỏ hàng');
            }
            
            await loadCart();
            shoeDetailModal.style.display = 'none';
            alert('Đã thêm sản phẩm vào giỏ hàng!');
        } catch (error) {
            console.error('Lỗi khi thêm vào giỏ hàng:', error);
            alert(error.message);
        }
    }
    
    async function updateCartItemQuantity(itemId, newQuantity) {
        if (newQuantity < 1) {
            if (confirm('Bạn có muốn xóa sản phẩm này khỏi giỏ hàng?')) {
                removeFromCart(itemId);
            }
            return;
        }
        
        try {
            const authOptions = {
                ...API_CONFIG.fetchOptions,
                method: 'PUT',
                headers: { 
                    ...API_CONFIG.fetchOptions.headers,
                    'Authorization': 'Basic ' + btoa(`${currentUser.username}:${currentUser.password || ''}`) 
                },
                body: JSON.stringify({ quantity: newQuantity })
            };
            
            const response = await fetch(`${API_BASE.cart}/${currentUser.username}/items/${itemId}`, authOptions);
            
            if (!response.ok) {
                throw new Error('Không thể cập nhật số lượng');
            }
            
            await loadCart();
            displayCart();
        } catch (error) {
            console.error('Lỗi khi cập nhật giỏ hàng:', error);
        }
    }
    
    async function removeFromCart(itemId) {
        try {
            const authOptions = {
                ...API_CONFIG.fetchOptions,
                method: 'DELETE',
                headers: { 
                    ...API_CONFIG.fetchOptions.headers,
                    'Authorization': 'Basic ' + btoa(`${currentUser.username}:${currentUser.password || ''}`) 
                }
            };
            
            const response = await fetch(`${API_BASE.cart}/${currentUser.username}/items/${itemId}`, authOptions);
            
            if (!response.ok) {
                throw new Error('Không thể xóa sản phẩm khỏi giỏ hàng');
            }
            
            await loadCart();
            displayCart();
        } catch (error) {
            console.error('Lỗi khi xóa khỏi giỏ hàng:', error);
        }
    }
    
    async function handleCheckout(e) {
        e.preventDefault();
        
        if (!currentUser) {
            alert('Vui lòng đăng nhập để đặt hàng');
            return;
        }
    
        if (cart.length === 0) {
            alert('Giỏ hàng của bạn đang trống!');
            return;
        }
    
        const formData = new FormData(checkoutForm);
        
        // Chuyển đổi chuỗi ngày thành định dạng yyyy-MM-ddTHH:mm:ss
        const deliveryDateInput = formData.get('deliveryDate');
        const deliveryDate = deliveryDateInput ? new Date(deliveryDateInput + 'T12:00:00').toISOString() : null;
        
        const orderData = {
            customerUsername: currentUser.username,
            customerName: formData.get('name'),
            shippingAddress: formData.get('address'), 
            customerAddress: formData.get('address'),   
            customerEmail: formData.get('email'),
            customerPhone: formData.get('phone'),
            deliveryDate: deliveryDate,
            paymentMethod: formData.get('paymentMethod'),
            items: cart.map(item => ({
                shoeId: item.shoeId,
                size: item.size,
                color: item.color,
                quantity: item.quantity,
                unitPrice: item.price
            }))
        };
        
        // Hiển thị chi tiết data để debug
        console.log('Sending order data:', JSON.stringify(orderData, null, 2));
        
        try {
            const authOptions = {
                ...API_CONFIG.fetchOptions,
                method: 'POST',
                headers: { 
                    ...API_CONFIG.fetchOptions.headers,
                    'Authorization': 'Basic ' + btoa(`${currentUser.username}:${currentUser.password || ''}`) 
                },
                body: JSON.stringify(orderData)
            };
            
            const response = await fetch(API_BASE.orders, authOptions);
            
            if (!response.ok) {
                const errorData = await response.json().catch(() => null);
                console.error('Error response:', errorData); // Hiển thị chi tiết lỗi từ server
                throw new Error(errorData?.message || 'Không thể tạo đơn hàng. Vui lòng thử lại sau.');
            }
            
            const order = await response.json();
            
            alert(`Đặt hàng thành công! Mã đơn hàng của bạn là ${order.id}`);
            
            // Reset cart
            await loadCart();
            
            // Reset forms
            checkoutForm.reset();
            
            // Show product list
            checkoutSection.classList.add('hidden');
            productSection.classList.remove('hidden');
        } catch (error) {
            console.error('Lỗi khi đặt hàng:', error);
            alert(error.message);
        }
    }
    
    function applyFilters() {
        const brandId = filterBrand.value ? parseInt(filterBrand.value) : null;
        const categoryId = filterCategory.value ? parseInt(filterCategory.value) : null;
        const gender = filterGender.value;
        const minPrice = filterPriceMin.value ? parseFloat(filterPriceMin.value) : null;
        const maxPrice = filterPriceMax.value ? parseFloat(filterPriceMax.value) : null;
        
        // Filter shoes
        const filteredShoes = shoes.filter(shoe => {
            if (brandId && (!shoe.brand || shoe.brand.id !== brandId)) return false;
            if (categoryId && (!shoe.category || shoe.category.id !== categoryId)) return false;
            if (gender && shoe.gender !== gender) return false;
            if (minPrice && shoe.price < minPrice) return false;
            if (maxPrice && shoe.price > maxPrice) return false;
            return true;
        });
        
        displayShoes(filteredShoes);
    }
    
    function resetFilters() {
        filterBrand.value = '';
        filterCategory.value = '';
        filterGender.value = '';
        filterPriceMin.value = '';
        filterPriceMax.value = '';
        
        displayShoes(shoes);
    }

    // Helper Functions
    function showCart() {
        displayCart();
        productSection.classList.add('hidden');
        checkoutSection.classList.add('hidden');
        cartSection.classList.remove('hidden');
    }
    
    function hideCart() {
        cartSection.classList.add('hidden');
        productSection.classList.remove('hidden');
    }
    
    function updateCartCount() {
        let count = 0;
        cart.forEach(item => {
            count += item.quantity;
        });
        cartCount.textContent = count;
    }
});
