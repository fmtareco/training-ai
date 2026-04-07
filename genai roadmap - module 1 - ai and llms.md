# **GenAI roadmap** - Module 1 - AI & LLM Crash Course

## Module 1 — AI & LLM Crash Course

### Learn

* AI vs ML vs LLMs
* What is a prompt
* Tokens, temperature

### Do

* Use OpenAI Playground
* Try:

  * summarization
  * Q&A
  * rewriting
  


### Goal

Understand:

* LLM = probabilistic text generator controlled by prompts

---

* view these concepts as a hierarchy of technologies moving from broad logic to specific mathematical probability.

----

## 1. AI vs. ML vs. LLMs (The Hierarchy)

* Think of this like a set of Russian nesting dolls. 
* Each one is a specialized version of the larger one.

* AI (Artificial Intelligence): 
	* The broadest category. 
	* It is any technique that enables computers to mimic human intelligence. 
	* This includes "Good Old Fashioned AI" (Rule-based systems like if/else trees for a chess game) and modern neural networks.
* ML (Machine Learning): 
	* A subset of AI. 
	* Instead of being explicitly programmed with rules, 
		* the system uses algorithms to learn patterns from data.
	* Example: 
		* A spam filter. 
			* You don't write rules for every "spammy" word; 
			* you show the model 10,000 spam emails, and 
				* it learns the statistical features of spam on its own.
* LLMs (Large Language Models): 
	* A very specific type of ML (specifically Deep Learning) trained on massive amounts of text. 
	* Their sole "job" is to predict the next most likely token in a sequence.
	* Example: 
		* GPT-4 or 
		* Llama 3.

------------------------------
## 2. What is a Prompt?

* In traditional programming, you provide Input Data and Code to get an Output. 
* In AI, the Prompt replaces the "Code" part of the equation.

* Definition: 
	* A prompt is the natural language instruction or context provided to an LLM to guide its output.
* Prompt Engineering: 
	* The craft of "programming" the model via text.
* Example:
	* Poor Prompt: 
		* "Write about cars."
	* Engineered Prompt: 
		* "Act as a mechanical engineer. 
		* Explain the benefits of electric vehicle regenerative braking to a 10-year-old using a bicycle analogy."

------------------------------
## 3. Tokens: The "Currency" of AI

* LLMs don't "read" words; they process Tokens. 
* A token is a chunk of text that the model recognizes.

* The Rule of Thumb: 1,000 tokens is roughly 750 words.
* Why they matter:
	* Cost: 
		* API providers (OpenAI, Anthropic) charge per 1M tokens.
	* Memory: 
		* The "Context Window" (the model's short-term memory) is measured in tokens.
	* Example: 
		* The word "Apple" is usually 1 token. 
		* A complex word like "Tokenization" might be split into 2 or 3 tokens: Token + iz + ation.

------------------------------
## 4. Temperature: The "Creativity" Knob

* Temperature (usually a scale from 0 to 2.0) 
* controls how much "risk" the model takes when picking the next word.

* Low Temperature (0.1 - 0.3): 
	* The model always picks the most mathematically probable next word.
	* Use Case: 
		* Factual Q&A, 
		* code generation, 
		* data extraction. 
		* It is stable and predictable.
* High Temperature (0.7 - 1.0+): 
	* The model is allowed to pick less likely words, 
	* leading to more "creative" or diverse outputs.
	* Use Case: 
		* Creative writing, 
		* brainstorming, 
		* roleplay.
* Visualizing the Math:
	* Sequence: "The cat sat on the..."
	   * Top Probabilities: "Mat" (80%), "Floor" (15%), "Pizza" (1%).
	   * Temp 0.1: Will almost always pick "Mat".
	   * Temp 1.5: Might pick "Pizza" just to be "different."

## Summary Table
--
| Concept | Simple Definition | Engineering Equivalent |
|---|---|---|
| AI | Machines acting smart | The System |
| ML | Learning from data patterns | The Algorithm |
| LLM | Predicting text sequences | The Engine/Library |
| Prompt | Human instructions | The Input/Function Call |
| Token | Chunks of text | The Data Unit (Bytes) |
| Temperature | Randomness setting | Variance/Entropy Control |


----

## Python example using LangChain 

* to see how these parameters are implemented in code
* uses LangChain, industry pattern to create LLM applications
* shows how to 
	* configure a modelo, 
	* define a prompt template 
	* set the temperature

## Python example (LangChain)
---
import os
from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate

# 1. Model Configuration (Level ML/LLM)
# we set the 'Temperature' to control randomness
# low values (0.1) turn the response deterministic and factual
llm = ChatOpenAI(
    model="gpt-4o",
    temperature=0.3,
    max_tokens=500  # Limit the 'Tokens' on response
)

# 2. Prompt Template definition 
# using placeholders ({topic}) to turn the prompt reusable
#
system_message = "És um assistente técnico especializado em {field}."
human_message = "Explica o conceito de {topic} para um engenheiro de software."
prompt_template = ChatPromptTemplate.from_messages([
    ("system", system_message),
    ("human", human_message)
])
# 3. Chain execution (AI Flow)
# operator '|' concats the prompt with the model (LangChain Expression Language - LCEL)
#
chain = prompt_template | llm

# final call passing the dynamic variables
#
response = chain.invoke({
    "field": "Sistemas Distribuídos",
    "topic": "Eventual Consistency"
})

print(response.content)

----

# OpenAI Playground

* to use OpenAI Playground, you first need an account at openai.com. 
* Once logged in, click on "Playground" in the left-hand menu.
* Think of the Playground as a IDE for Prompts. 
* It allows you to test models without writing a single line of code.


## Video Open AI Playground Tutorial for Beginners | OpenAI ChatGPT Alternative

https://www.youtube.com/watch?v=QpM5pr-jS9I
* Intro
* OpenAI Playground VS ChatGPT
* How to Create an Account
* Manage Usage & Free Credits
* Playground Quick Demo
* Save Preset
* Speech to Text
* Transcribe Automatically from Audio Files
* Create Quizzes
* Settings - Model Type
* Settings - Temperature
* Settings - Maximum length
* ChatGPT Limitations
* Playground  Presets and Examples
* Q&A Model
* Summarize Text into Simpler Concepts
* Translate English to Other Languages 
* Grammar Correction
* Spreadsheet Creator
* Movie to Emoji
* AI Chat
* Mood to Color


## 1. The Interface Setup
Before trying the examples, configure the right sidebar:

* Mode: Chat (Standard for most tasks).
* Model: gpt-4o (Balanced and fast) or gpt-3.5-turbo (Cheaper).
* Temperature: Set to 0.5 for a balance of accuracy and flow.
* System Instructions: This is the "Base Persona." Enter: "You are a versatile assistant skilled in editing, analysis, and direct communication."

------------------------------
## 2. Task: Summarization
Summarization is about Information Density. You want the core value without the "fluff."
The Prompt (Paste into User box):

"Summarize the following text into three bullet points. Focus only on technical specifications and ignore marketing language.
[Insert a long product description or article here]"


* Pro Tip: If the summary is too long, reduce the Maximum Length slider in the sidebar.

------------------------------
## 3. Task: Q&A (Question & Answering)
For Q&A, accuracy is key. You can provide a "Source of Truth" to prevent the model from hallucinating.
The Prompt:

"Answer the question based strictly on the provided context. If the answer isn't there, say 'I do not have enough information.'
Context: The company's remote work policy allows for up to 2 weeks of 'work from anywhere' per year, provided the timezone difference is less than 4 hours.
Question: Can I work from Tokyo for a month if I live in London?"


* Expected Result: The model should say "No" because it exceeds 2 weeks and the timezone difference is likely more than 4 hours.

------------------------------
## 4. Task: Rewriting / Transformation
This is where the Temperature setting shines. Use a low temp for professional edits and a high temp for creative styles.
The Prompt:

"Rewrite the following email to be more professional, concise, and firm.
Original: 'Hey, just checking in because you haven't sent that report yet and I really need it by tomorrow or my boss will be mad. Please send it ASAP! thanks.'"


* Experiment: Try changing the instructions to "Rewrite this in the style of a 1920s noir detective" to see how the model's creativity changes.

------------------------------
## Summary of Playground Controls for these Tasks:

| Task | Recommended Temperature | Recommended Model |
|---|---|---|
| Summarization | 0.3 (Predictable) | gpt-4o-mini |
| Q&A | 0.0 (Strictly factual) | gpt-4o |
| Rewriting | 0.7 - 1.0 (Creative) | gpt-4o |


