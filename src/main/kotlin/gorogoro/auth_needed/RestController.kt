package gorogoro.auth_needed

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class ControllerHello {

    @GetMapping("/hello")
    @PreAuthorize("hasRole('tester')")
    fun sayHello(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello")
    }

    @GetMapping("/admin")
    fun sayHelloToAdmin(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello Admin")
    }

    @GetMapping("/user")
    fun sayHelloToUser(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello User")
    }
}