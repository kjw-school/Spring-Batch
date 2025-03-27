package org.kjw.springbatch.chapter04;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication

public class HelloWorld {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("basicJob")
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() {
		return this.stepBuilderFactory.get("step1")
			.tasklet((contribution, chunkContext) -> {
				System.out.println("Hello, world!");
				return RepeatStatus.FINISHED;
			}).build();
	}

	/**
	 * @EnablebatchProcessing 애너테이션 이 애너테이션은 애플리케이션 내에서 한 번만 적용하면 되며, 배치 잡 수행에 필요한 인프라스트럭처를 제공한다.<br>
	 * JobBuilderFactory와 StepBuilderFactory 이 두 팩토리는 JobBuilder와 StepBuilder 인스턴스를 각각 생성하며, JobBuilder와 StepBuilder가 실제로 스프링 배치 잡과 스텝을 생성하는데 사용된다.<br>
	 * 이 팩토리 메서드는 완전하게 구성된 스프링 배치 잡을 반환한다. 잡 자체는 스프링 배치가 제공하는 빌더를 통해 구성된다.<br>
	 * 즉, JobBuilderFactory.get 메서드를 호출하면서 잡 이름을 전달하면 JobBuilder를 얻을 수 있으며, 이 빌더를 사용해 잡을 구성할 수 있다.<br>
	 * Step 타입을 반환하는 메서드를 작성하고 메서드 내에서 stepBuilderFactory를 사용해 스텝 구성을 한다.<br>
	 * get 메서드를 호출하면서 스텝 이름을 전달하면 StepBuilder가 반환되며 이 빌더를 사용해 스텝을 정의할 수 있다.
	 */
	public static void main(String[] args) {
		SpringApplication.run(HelloWorld.class, args);
	}

}
