import json
import os
from datetime import datetime
from collections import defaultdict
import matplotlib.pyplot as plt

FILE = "expenses.json"

def load_data():
    if not os.path.exists(FILE):
        return []
    with open(FILE, "r") as f:
        return json.load(f)

def save_data(data):
    with open(FILE, "w") as f:
        json.dump(data, f, indent=4)

def add_expense():
    data = load_data()

    date = input("Enter date (YYYY-MM-DD) [Enter for today]: ")
    if not date:
        date = str(datetime.now().date())

    category = input("Enter category (Food/Travel/Bills/Other): ")
    amount = float(input("Enter amount: "))
    desc = input("Enter description: ")

    expense = {
        "date": date,
        "category": category,
        "amount": amount,
        "description": desc
    }

    data.append(expense)
    save_data(data)

    print("✅ Expense added successfully!")

def view_expenses():
    data = load_data()

    if not data:
        print("⚠️ No expenses found.")
        return

    print("\n📋 All Expenses:")
    for exp in data:
        print(f"{exp['date']} | {exp['category']} | ₹{exp['amount']} | {exp['description']}")

def monthly_summary():
    data = load_data()

    if not data:
        print("⚠️ No data available.")
        return None

    month = input("Enter month (MM): ")

    total = 0
    category_sum = defaultdict(float)

    for exp in data:
        if exp["date"][5:7] == month:
            total += exp["amount"]
            category_sum[exp["category"]] += exp["amount"]

    if total == 0:
        print("⚠️ No data for this month.")
        return None

    print(f"\n💰 Total Spending: ₹{total}")
    print("\n📊 Category Breakdown:")
    for cat, amt in category_sum.items():
        print(f"{cat}: ₹{amt}")

    return category_sum, total

def highest_category(category_sum):
    max_cat = max(category_sum, key=category_sum.get)
    print(f"\n🔥 Highest Spending Category: {max_cat} (₹{category_sum[max_cat]})")
    return max_cat

def show_pie(category_sum):
    labels = list(category_sum.keys())
    values = list(category_sum.values())

    plt.pie(values, labels=labels, autopct='%1.1f%%')
    plt.title("Expense Distribution")
    plt.show()

def insights(category_sum, total):
    max_cat = max(category_sum, key=category_sum.get)

    print("\n💡 Insights:")
    print(f"- You spent most on {max_cat}. Try reducing it.")

    if total > 10000:
        print("- ⚠️ Warning: You exceeded ₹10,000 this month!")

def main():
    while True:
        print("\n====== 💰 Expense Tracker ======")
        print("1. Add Expense")
        print("2. View Expenses")
        print("3. Monthly Summary & Insights")
        print("4. Exit")

        choice = input("Enter your choice: ")

        if choice == "1":
            add_expense()

        elif choice == "2":
            view_expenses()

        elif choice == "3":
            result = monthly_summary()
            if result:
                category_sum, total = result
                highest_category(category_sum)
                insights(category_sum, total)
                show_pie(category_sum)

        elif choice == "4":
            print("👋 Exiting... Bye!")
            break

        else:
            print("❌ Invalid choice. Try again.")

if __name__ == "__main__":
    main()