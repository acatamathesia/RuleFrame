# ============================================
# RuleFrame 接口测试运行脚本
# ============================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  RuleFrame 接口测试运行器" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 进入项目目录
$projectDir = "d:\project\RuleFrameProject\RuleFrame"
Set-Location $projectDir

Write-Host "📁 项目目录: $projectDir" -ForegroundColor Yellow
Write-Host ""

# 显示测试菜单
Write-Host "请选择要运行的测试：" -ForegroundColor Green
Write-Host "1. 运行所有测试" -ForegroundColor White
Write-Host "2. 认证接口测试 (AuthControllerTest)" -ForegroundColor White
Write-Host "3. 用户管理测试 (UserControllerTest)" -ForegroundColor White
Write-Host "4. 菜单管理测试 (MenuControllerTest)" -ForegroundColor White
Write-Host "5. 角色管理测试 (RoleControllerTest)" -ForegroundColor White
Write-Host "6. 系统监控测试 (SystemControllerTest)" -ForegroundColor White
Write-Host "7. 规则引擎测试 (RuleControllerTest)" -ForegroundColor White
Write-Host "8. 退出" -ForegroundColor White
Write-Host ""

$choice = Read-Host "请输入选项 (1-8)"

switch ($choice) {
    "1" {
        Write-Host ""
        Write-Host "🚀 开始运行所有测试..." -ForegroundColor Green
        Write-Host ""
        mvn test
    }
    "2" {
        Write-Host ""
        Write-Host "🔐 运行认证接口测试..." -ForegroundColor Green
        Write-Host ""
        mvn test -Dtest=AuthControllerTest
    }
    "3" {
        Write-Host ""
        Write-Host "👥 运行用户管理测试..." -ForegroundColor Green
        Write-Host ""
        mvn test -Dtest=UserControllerTest
    }
    "4" {
        Write-Host ""
        Write-Host "📋 运行菜单管理测试..." -ForegroundColor Green
        Write-Host ""
        mvn test -Dtest=MenuControllerTest
    }
    "5" {
        Write-Host ""
        Write-Host "🎭 运行角色管理测试..." -ForegroundColor Green
        Write-Host ""
        mvn test -Dtest=RoleControllerTest
    }
    "6" {
        Write-Host ""
        Write-Host "💻 运行系统监控测试..." -ForegroundColor Green
        Write-Host ""
        mvn test -Dtest=SystemControllerTest
    }
    "7" {
        Write-Host ""
        Write-Host "⚙️ 运行规则引擎测试..." -ForegroundColor Green
        Write-Host ""
        mvn test -Dtest=RuleControllerTest
    }
    "8" {
        Write-Host ""
        Write-Host "👋 退出测试运行器" -ForegroundColor Yellow
        exit
    }
    default {
        Write-Host ""
        Write-Host "❌ 无效的选项，请重新运行脚本" -ForegroundColor Red
        exit
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  测试完成！" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "📊 测试报告位置: target/surefire-reports/" -ForegroundColor Yellow
Write-Host ""

# 询问是否查看报告
$viewReport = Read-Host "是否查看测试报告？(y/n)"
if ($viewReport -eq "y" -or $viewReport -eq "Y") {
    $reportDir = "target\surefire-reports"
    if (Test-Path $reportDir) {
        Write-Host ""
        Write-Host "📄 测试报告列表：" -ForegroundColor Green
        Get-ChildItem $reportDir -Filter "*.txt" | ForEach-Object {
            Write-Host "  - $($_.Name)" -ForegroundColor White
        }
        Write-Host ""
        $reportName = Read-Host "请输入要查看的报告文件名"
        $reportPath = Join-Path $reportDir $reportName
        if (Test-Path $reportPath) {
            Get-Content $reportPath
        } else {
            Write-Host "❌ 报告文件不存在" -ForegroundColor Red
        }
    } else {
        Write-Host "❌ 测试报告目录不存在" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "按任意键退出..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
