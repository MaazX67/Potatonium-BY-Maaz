#include <filesystem>
#include <iostream>
#include <optional>
#include <string>
#include <vector>

#ifdef _WIN32
#include <windows.h>
#include <shellapi.h>

namespace {
constexpr int kInstallButton = 1001;
constexpr int kOpenLauncherButton = 1002;
constexpr int kOpenModsButton = 1003;
HWND g_status = nullptr;

std::optional<std::filesystem::path> environmentPath(const wchar_t* name) {
    const DWORD required = GetEnvironmentVariableW(name, nullptr, 0);
    if (required == 0) {
        return std::nullopt;
    }

    std::wstring value(required, L'\0');
    const DWORD written = GetEnvironmentVariableW(name, value.data(), required);
    if (written == 0 || written >= required) {
        return std::nullopt;
    }
    value.resize(written);
    return std::filesystem::path(value);
}

std::filesystem::path minecraftDirectory() {
    const auto appData = environmentPath(L"APPDATA");
    return appData ? *appData / L".minecraft" : std::filesystem::path{};
}

std::filesystem::path executableDirectory() {
    std::wstring path(32768, L'\0');
    const DWORD length = GetModuleFileNameW(nullptr, path.data(), static_cast<DWORD>(path.size()));
    if (length == 0 || length >= path.size()) {
        return {};
    }
    path.resize(length);
    return std::filesystem::path(path).parent_path();
}

std::optional<std::filesystem::path> findModJar() {
    const auto app = executableDirectory();
    const std::vector<std::filesystem::path> candidates = {
        app / L"MaazXClient.jar",
        app / L"mod" / L"build" / L"libs",
        app.parent_path() / L"mod" / L"build" / L"libs"
    };

    for (const auto& candidate : candidates) {
        if (std::filesystem::is_regular_file(candidate)) {
            return candidate;
        }
        if (!std::filesystem::is_directory(candidate)) {
            continue;
        }

        std::error_code error;
        std::filesystem::directory_iterator iterator(candidate, error);
        const std::filesystem::directory_iterator end;
        if (error) {
            continue;
        }
        for (; iterator != end; iterator.increment(error)) {
            if (error) {
                break;
            }
            const auto& entry = *iterator;
            if (!entry.is_regular_file()) {
                continue;
            }
            const auto filename = entry.path().filename().wstring();
            if (filename.rfind(L"maazxclient-", 0) == 0 && entry.path().extension() == L".jar"
                    && filename.find(L"sources") == std::wstring::npos) {
                return entry.path();
            }
        }
    }
    return std::nullopt;
}

void setStatus(const std::wstring& message) {
    if (g_status != nullptr) {
        SetWindowTextW(g_status, message.c_str());
    }
}

void installMod() {
    const auto modJar = findModJar();
    const auto gameDirectory = minecraftDirectory();
    if (!modJar || gameDirectory.empty() || !std::filesystem::is_directory(gameDirectory)) {
        MessageBoxW(nullptr,
                    L"MaazXClient or the standard Minecraft Java Edition folder was not found.",
                    L"MaazXLauncher",
                    MB_OK | MB_ICONWARNING);
        return;
    }

    const auto modsDirectory = gameDirectory / L"mods";
    std::error_code error;
    std::filesystem::create_directories(modsDirectory, error);
    if (error) {
        MessageBoxW(nullptr, L"Could not create the Minecraft mods folder.", L"MaazXLauncher",
                    MB_OK | MB_ICONERROR);
        return;
    }

    std::filesystem::copy_file(*modJar, modsDirectory / L"maazxclient.jar",
                               std::filesystem::copy_options::overwrite_existing, error);
    if (error) {
        MessageBoxW(nullptr, L"Could not install MaazXClient in the Minecraft mods folder.",
                    L"MaazXLauncher", MB_OK | MB_ICONERROR);
        return;
    }
    setStatus(L"MaazXClient installed. Select a Fabric 1.20.1 profile in Minecraft Launcher.");
}

void openMinecraftLauncher() {
    const auto programFiles = environmentPath(L"ProgramFiles");
    const auto programFilesX86 = environmentPath(L"ProgramFiles(x86)");
    const auto localAppData = environmentPath(L"LOCALAPPDATA");
    std::vector<std::filesystem::path> candidates;
    if (programFilesX86) {
        candidates.push_back(*programFilesX86 / L"Minecraft Launcher" / L"MinecraftLauncher.exe");
    }
    if (programFiles) {
        candidates.push_back(*programFiles / L"Minecraft Launcher" / L"MinecraftLauncher.exe");
    }
    if (localAppData) {
        candidates.push_back(*localAppData / L"Programs" / L"Minecraft Launcher" / L"MinecraftLauncher.exe");
    }

    for (const auto& candidate : candidates) {
        if (std::filesystem::is_regular_file(candidate)) {
            const auto result = reinterpret_cast<INT_PTR>(
                    ShellExecuteW(nullptr, L"open", candidate.c_str(), nullptr, nullptr, SW_SHOWNORMAL));
            if (result > 32) {
                return;
            }
        }
    }

    const auto protocolResult = reinterpret_cast<INT_PTR>(
            ShellExecuteW(nullptr, L"open", L"minecraft://", nullptr, nullptr, SW_SHOWNORMAL));
    if (protocolResult <= 32) {
        MessageBoxW(nullptr,
                    L"Install or open the official Minecraft Launcher, then choose a Fabric 1.20.1 profile.",
                    L"MaazXLauncher", MB_OK | MB_ICONINFORMATION);
    }
}

void openModsFolder() {
    const auto gameDirectory = minecraftDirectory();
    if (gameDirectory.empty() || !std::filesystem::is_directory(gameDirectory)) {
        MessageBoxW(nullptr, L"Could not find the standard Minecraft Java Edition folder.",
                    L"MaazXLauncher", MB_OK | MB_ICONWARNING);
        return;
    }
    const auto directory = gameDirectory / L"mods";
    std::error_code error;
    std::filesystem::create_directories(directory, error);
    if (error) {
        MessageBoxW(nullptr, L"Could not open the Minecraft mods folder.", L"MaazXLauncher",
                    MB_OK | MB_ICONERROR);
        return;
    }
    ShellExecuteW(nullptr, L"open", directory.c_str(), nullptr, nullptr, SW_SHOWNORMAL);
}

LRESULT CALLBACK windowProcedure(HWND window, UINT message, WPARAM wParam, LPARAM lParam) {
    switch (message) {
    case WM_CREATE:
        CreateWindowW(L"STATIC", L"MaazXClient  |  Minecraft 1.20.1",
                      WS_CHILD | WS_VISIBLE, 24, 20, 340, 28, window, nullptr, nullptr, nullptr);
        g_status = CreateWindowW(L"STATIC", L"Install the client mod, then start your Fabric profile.",
                                 WS_CHILD | WS_VISIBLE, 24, 56, 390, 42, window, nullptr, nullptr, nullptr);
        CreateWindowW(L"BUTTON", L"Install MaazXClient",
                      WS_CHILD | WS_VISIBLE | BS_PUSHBUTTON, 24, 116, 190, 36,
                      window, reinterpret_cast<HMENU>(static_cast<INT_PTR>(kInstallButton)), nullptr, nullptr);
        CreateWindowW(L"BUTTON", L"Open Minecraft Launcher",
                      WS_CHILD | WS_VISIBLE | BS_PUSHBUTTON, 224, 116, 190, 36,
                      window, reinterpret_cast<HMENU>(static_cast<INT_PTR>(kOpenLauncherButton)), nullptr, nullptr);
        CreateWindowW(L"BUTTON", L"Open mods folder",
                      WS_CHILD | WS_VISIBLE | BS_PUSHBUTTON, 24, 164, 190, 32,
                      window, reinterpret_cast<HMENU>(static_cast<INT_PTR>(kOpenModsButton)), nullptr, nullptr);
        return 0;
    case WM_COMMAND:
        switch (LOWORD(wParam)) {
        case kInstallButton:
            installMod();
            return 0;
        case kOpenLauncherButton:
            openMinecraftLauncher();
            return 0;
        case kOpenModsButton:
            openModsFolder();
            return 0;
        default:
            break;
        }
        break;
    case WM_DESTROY:
        PostQuitMessage(0);
        return 0;
    default:
        break;
    }
    return DefWindowProcW(window, message, wParam, lParam);
}
}

int WINAPI wWinMain(HINSTANCE instance, HINSTANCE, PWSTR, int showCommand) {
    SetProcessDPIAware();
    const wchar_t className[] = L"MaazXLauncherWindow";
    WNDCLASSW windowClass{};
    windowClass.lpfnWndProc = windowProcedure;
    windowClass.hInstance = instance;
    windowClass.hCursor = LoadCursorW(nullptr, IDC_ARROW);
    windowClass.hbrBackground = reinterpret_cast<HBRUSH>(COLOR_WINDOW + 1);
    windowClass.lpszClassName = className;

    if (!RegisterClassW(&windowClass)) {
        return 1;
    }
    HWND window = CreateWindowExW(0, className, L"MaazXLauncher",
                                  WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU | WS_MINIMIZEBOX,
                                  CW_USEDEFAULT, CW_USEDEFAULT, 460, 250, nullptr, nullptr,
                                  instance, nullptr);
    if (window == nullptr) {
        return 1;
    }

    ShowWindow(window, showCommand);
    UpdateWindow(window);
    MSG message{};
    while (GetMessageW(&message, nullptr, 0, 0) > 0) {
        TranslateMessage(&message);
        DispatchMessageW(&message);
    }
    return static_cast<int>(message.wParam);
}
#else
int main() {
    std::cout << "MaazXLauncher is a Windows desktop companion. Build it on Windows to install the mod "
                 "and open the official Minecraft Launcher.\n";
    return 0;
}
#endif
