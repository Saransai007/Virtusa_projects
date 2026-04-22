CREATE TABLE Customers (
    customer_id INT PRIMARY KEY,
    name VARCHAR(100),
    city VARCHAR(100)
);

CREATE TABLE Products (
    product_id INT PRIMARY KEY,
    name VARCHAR(100),
    category VARCHAR(100),
    price DECIMAL(10,2)
);

CREATE TABLE Orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    date DATE default CURRENT_DATE,
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
);

CREATE TABLE Order_Items (
    order_id INT,
    product_id INT,
    quantity INT,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);
1. List all the products that have been ordered Most
SELECT pro.product_id,pro.name from products pro join order_items o
on pro.product_id=o.product_id
GROUP BY pro.name,pro.product_id 
Having SUM(o.quantity)=(
	SELECT MAX(total_quantity) from (
		SELECT SUM(quantity) as total_quantity
		from order_items
		GROUP BY product_id
	) t
);
2. Find the total value of orders placed by each customer and identify the customer with the highest total order value.
select cu.customer_id,cu.name,SUM(o.quantity * pro.price) as value_total
from customers cu join orders oi
on cu.customer_id=oi.customer_id join order_items o on o.order_id=oi.order_id
join products pro on pro.product_id=o.product_id
group by cu.customer_id,cu.name
Having SUM(o.quantity * pro.price) =(
	select max(total_price) From(
		select SUM(o.quantity*pro.price) as total_price
		from orders oi join order_items o ON o.order_id = oi.order_id
        JOIN products pro ON pro.product_id = o.product_id
        GROUP BY oi.customer_id
	) a
);
3. Calculate the monthly sales revenue 
SELECT
DATE_TRUNC('month',oi.date)AS month,SUM(o.quantity*pro.price)
from orders oi join order_items o on oi.order_id=o.order_id
join products pro on pro.product_id=o.product_id
GROUP BY DATE_TRUNC('month',oi.date)
ORDER BY month;
4.	Category-wise sales analysis
select pro.category,sum(pro.price*o.quantity) as total_revenue
from products pro join order_items o 
on pro.product_id=o.product_id
GROUP BY pro.category
ORDER BY sum(pro.price*o.quantity) DESC; 
5. Identify customers who have not placed any orders in the last 2 years .
SELECT cus.name
FROM customers cus
WHERE cus.customer_id NOT IN (
    SELECT customer_id
    FROM orders
    WHERE date >= CURRENT_DATE - INTERVAL '1 year'
);