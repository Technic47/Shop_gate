package ru.kuznetsov.shop.gate.service;

import ru.kuznetsov.shop.gate.dto.LoginPasswordDto;

public interface AuthService {

    String loginAuth(LoginPasswordDto authHeader);
}
