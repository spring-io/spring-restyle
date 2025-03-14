package io.spring.openrewrite.format;

import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.Recipe;
import org.openrewrite.config.Environment;
import org.openrewrite.java.JavaParser;
import org.openrewrite.style.NamedStyles;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

public class StyleTests implements RewriteTest {
	private String projectRootPackage = "org.springframework";

	@Override
	public void defaults(RecipeSpec spec) {
		Properties p = new Properties();
		p.put("restyle.projectRootPackage", this.projectRootPackage);
		p.put("restyle.inceptionYear", "2002");
		Environment env = Environment.builder(p)
			.scanRuntimeClasspath()
			.build();
		Recipe springFormatRecipe = env.activateRecipes("io.spring.Format");
		List<NamedStyles> styles = env.activateStyles("io.spring.Style");
		spec
			.recipe(springFormatRecipe)
				.parser(JavaParser.fromJavaVersion()
					.classpath("spring-core", "spring-security-core")
					.styles(styles)
				);
	}

	@Test
	void importOrderWhenFramework() {
		rewriteRun(
			java(
				"""
				/*
				 * License
				 */
				
				package example;
				
				import java.util.Collections;
				import org.springframework.core.SpringVersion;
				import java.util.Arrays;
				import javax.crypto.SecretKey;
				
				class A {
					Arrays a;
					Collections c;
					SpringVersion sv;
					SecretKey sk;
				}""",
				"""
				/*
				 * License
				 */
				
				package example;
				
				import java.util.Arrays;
				import java.util.Collections;
				
				import javax.crypto.SecretKey;
				
				import org.springframework.core.SpringVersion;
				
				class A {
					Arrays a;
					Collections c;
					SpringVersion sv;
					SecretKey sk;
				}"""
			)
		);
	}

	@Test
	void importOrderWhenSpringSecurity() {
		this.projectRootPackage = "org.springframework.security";
		rewriteRun(
			java("""
				/*
				 * License
				 */
				
				package example;
				
				import java.util.Collections;
				import org.springframework.security.core.Authentication;
				import java.util.Arrays;
				import org.springframework.core.SpringVersion;
				import javax.crypto.SecretKey;
				
				class A {
					Authentication authn;
					Arrays a;
					Collections c;
					SpringVersion sv;
					SecretKey sk;
				}""",
			"""
				/*
				 * License
				 */
				
				package example;
				
				import java.util.Arrays;
				import java.util.Collections;
				
				import javax.crypto.SecretKey;
				
				import org.springframework.core.SpringVersion;
				
				import org.springframework.security.core.Authentication;
				
				class A {
					Authentication authn;
					Arrays a;
					Collections c;
					SpringVersion sv;
					SecretKey sk;
				}"""
			)
		);
	}

	@Test
	void removeStarImports() {
		rewriteRun(
			java("""
				/*
				 * License
				 */
				
				package example;
				
				import java.util.*;
				
				class A {
					Arrays a;
					Collections c;
				}""",
				"""
				/*
				 * License
				 */
				
				package example;
				
				import java.util.Arrays;
				import java.util.Collections;
				
				class A {
					Arrays a;
					Collections c;
				}""")
		);
	}

	@Test
	void removeUnusedImports() {
		rewriteRun(
			java(
				"""
				/*
				 * License
				 */
				
				package example;
				
				import java.util.Collections;
				import java.util.Arrays;
				
				class A {
				}""",
				"""
				/*
				 * License
				 */
				
				package example;
				
				class A {
				}""")
		);
	}

	@Test
	void spacesToTabs() {
		rewriteRun(
			java(
				"""
				/*
				 * License
				 */
				
				package example;
				
				class A {
				    public static void a() {
				    }
				}""",
				"""
				/*
				 * License
				 */
				
				package example;
				
				class A {
					public static void a() {
					}
				}""")
		);
	}

	@Disabled // FIXME: This needs to be fixed
	@Test
	void trimTrailingWhitespace() {
		rewriteRun(
			java(
				"""
				/*
				 * License
				 */
				
				package example;
				
				class A {
					public static void a() { 
					}
				}""",
				"""
				/*
				 * License
				 */
				
				package example;
				
				class A {
					public static void a() {
					}
				}"""
			)
		);
	}

	@Test
	@Disabled // FIXME: This should be enabled
	void blankLineBeforePackage() {
		rewriteRun(
			java(
				"""
				/*
				 * License
				 */
				package example;
				
				class A {
					public static void a() {
					}
				}""",
				"""
				/*
				 * License
				 */
				
				package example;
				
				class A {
					public static void a() {
					}
				}"""
			)
		);
	}

	@Test
	void blankLineAfterPackage() {
		rewriteRun(
			java(
			"""
				/*
				 * License
				 */
				
				package example;
				class A {
					public static void a() {
					}
				}""",
				"""
				/*
				 * License
				 */
				
				package example;
				
				class A {
					public static void a() {
					}
				}"""
			)
		);
	}
}
