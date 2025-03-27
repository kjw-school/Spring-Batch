package org.kjw.springbatch.chapter04;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class HelloWorldJob3 {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job job3() {
		return this.jobBuilderFactory.get("basicJob")
			.start(step3())
			.build();
	}

	@Bean
	public Step step3() {
		return this.stepBuilderFactory.get("step3")
			.tasklet(helloWorldTasklet2(null))
			.build();
	}

	// @StepScope, 스프링 배치에 포함된 이 커스템 스텝 스코프와 잡 스코프를 사용하면 늦은 바인딩 기능을 쉽게 사용할 수 있다.
	// 이 스코프의 각각의 기능은 스텝의 실행 범위(스텝 스코프)나 잡의 실행 범위(잡 스코프)에 들어갈 때까지 빈 생성을 지연시키는 것이다.
	@Bean
	public Tasklet helloWorldTasklet2(@Value("#{jobParameters['name']}") String name) {
		return (contribution, chunkContext) -> {
			System.out.println(String.format("Hello, %s!", name));
			return RepeatStatus.FINISHED;
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloWorldJob.class, args);
	}

}
