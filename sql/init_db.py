import mysql.connector
import json

conn = mysql.connector.connect(
    host="172.17.160.113",
    user="nkd",
    password="123456",
    database="ruleframe",
    charset="utf8mb4",
    autocommit=True
)
cur = conn.cursor()

print("Dropping existing tables...")
cur.execute("DROP TABLE IF EXISTS rule_def")
cur.execute("DROP TABLE IF EXISTS rule_group_def")
print("Dropped.")

print("Creating rule_group_def...")
cur.execute("""
CREATE TABLE rule_group_def (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_code VARCHAR(64) NOT NULL,
    group_name VARCHAR(128) NOT NULL,
    strategy VARCHAR(32) NOT NULL DEFAULT 'ALL_MATCH',
    description VARCHAR(512) DEFAULT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_group_code (group_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
""")
print("rule_group_def created.")

print("Creating rule_def...")
cur.execute("""
CREATE TABLE rule_def (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_id BIGINT NOT NULL,
    rule_code VARCHAR(64) NOT NULL,
    rule_name VARCHAR(128) NOT NULL,
    priority INT NOT NULL DEFAULT 0,
    conditions_json TEXT DEFAULT NULL,
    result_action VARCHAR(64) DEFAULT NULL,
    result_message VARCHAR(256) DEFAULT NULL,
    unified_return TINYINT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_group_id (group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
""")
print("rule_def created.")

print("Inserting test data...")
cur.execute(
    "INSERT INTO rule_group_def (group_code, group_name, strategy, description, status) VALUES (%s, %s, %s, %s, %s)",
    ("USER_ADMISSION", "用户准入校验", "ALL_MATCH", "对新注册用户进行多维度的准入资格审查", 1)
)
print("Group inserted.")

cur.execute("SELECT id FROM rule_group_def WHERE group_code = %s", ("USER_ADMISSION",))
gid = cur.fetchone()[0]
print(f"Group ID: {gid}")

c1 = json.dumps({
    "logicalOperator": "AND",
    "conditions": [
        {"element": "inBlacklist", "operator": "EQUAL", "expectedValue": False,
         "failureCode": "BLACKLISTED", "failureMessage": "用户在黑名单中"}
    ]
}, ensure_ascii=False)

c2 = json.dumps({
    "logicalOperator": "AND",
    "conditions": [
        {"element": "age", "operator": "GREATER_THAN_OR_EQUAL", "expectedValue": 18,
         "failureCode": "AGE_TOO_YOUNG", "failureMessage": "用户年龄必须不小于18岁"}
    ]
}, ensure_ascii=False)

c3 = json.dumps({
    "logicalOperator": "AND",
    "conditions": [
        {"element": "creditScore", "operator": "GREATER_THAN", "expectedValue": 600,
         "failureCode": "LOW_CREDIT", "failureMessage": "信用评分必须大于600"}
    ]
}, ensure_ascii=False)

c4 = json.dumps({
    "logicalOperator": "OR",
    "conditions": [
        {"element": "vipLevel", "operator": "GREATER_THAN", "expectedValue": 0,
         "failureCode": "NOT_VIP", "failureMessage": "非VIP用户"},
        {"element": "totalSpending", "operator": "GREATER_THAN", "expectedValue": 50000,
         "failureCode": "LOW_SPENDING", "failureMessage": "累计消费不足50000"}
    ]
}, ensure_ascii=False)

rules = [
    (gid, "BLACKLIST_CHECK", "黑名单检查", 0, c1, "REJECT", "用户已被列入黑名单", 1),
    (gid, "AGE_CHECK", "年龄检查", 1, c2, "REJECT", "年龄不符合要求", 1),
    (gid, "CREDIT_CHECK", "信用评分检查", 2, c3, "REJECT", "信用评分不足", 1),
    (gid, "VIP_OR_PREMIUM", "VIP或高消费用户检查", 3, c4, "APPROVE", "VIP或高消费用户通过", 1),
]
for r in rules:
    cur.execute(
        "INSERT INTO rule_def (group_id, rule_code, rule_name, priority, conditions_json, result_action, result_message, status) "
        "VALUES (%s, %s, %s, %s, %s, %s, %s, %s)", r
    )
print("Rules inserted.")

print("Updating menus...")
cur.execute("SELECT id FROM sys_menu WHERE menu_code = %s", ("rules",))
row = cur.fetchone()
if row:
    pid = row[0]
    cur.execute(
        "INSERT IGNORE INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, permission, status, visible) "
        "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
        (pid, "规则组管理", "ruleGroup", 2, "/ruleEngine/RuleGroup", "ruleEngine/RuleGroup", "Collection", 1, "rule:group:list", 1, 1)
    )
    cur.execute(
        "INSERT IGNORE INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, permission, status, visible) "
        "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
        (pid, "规则测试", "ruleTester", 2, "/ruleEngine/RuleTester", "ruleEngine/RuleTester", "VideoPlay", 2, "rule:tester:execute", 1, 1)
    )
    cur.execute(
        "INSERT IGNORE INTO sys_role_menu (role_id, menu_id) "
        "SELECT 1, id FROM sys_menu WHERE menu_code IN (%s, %s) AND deleted = 0",
        ("ruleGroup", "ruleTester")
    )
    print("Menus added.")

print("Verifying...")
cur.execute("SELECT COUNT(*) FROM rule_group_def")
print(f"Rule groups count: {cur.fetchone()[0]}")
cur.execute("SELECT COUNT(*) FROM rule_def")
print(f"Rules count: {cur.fetchone()[0]}")
cur.execute("SELECT id, group_code, group_name FROM rule_group_def")
for row in cur.fetchall():
    print(f"  Group: {row}")

cur.close()
conn.close()
print("All done!")
